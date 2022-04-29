package it.unipd.pdp2021.ex1;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

/**
 * A judge examines the athlete on the screen and emits votes.
 *
 * <p>The judge will end its activity after it has seen the final element and emitted all the votes.
 */
public class Judge implements Runnable {

  private final BlockingQueue<Element> screen;
  private final BlockingQueue<Vote> votes;
  private final Thread voting;
  public final int idx;
  public final Nation nation;

  /**
   * @param nation The nation of the judge (contains the voting data)
   * @param idx Index of the judge
   * @param screen Screen that the judges observes for elements
   * @param votes Channel where the judge will emit votes
   */
  public Judge(Nation nation, int idx, BlockingQueue<Element> screen, BlockingQueue<Vote> votes) {
    this.screen = screen;
    this.votes = votes;
    this.idx = idx;
    this.nation = nation;
    this.voting = new Thread(this);
  }

  /** Call to start this judge's activity */
  void start() {
    voting.start();
  }

  /** Call to check if the voting is over */
  boolean done() {
    return !voting.isAlive();
  }

  @Override
  public void run() {

    Random rnd = new Random();
    String lastElement;
    Set<Vote> reviews = new HashSet<>();

    try {

      // look at elements, and grade them
      do {
        Element e = screen.take();
        lastElement = e.element();
        if (!Element.END.equals(e.element())) {
          boolean review = nation.review.contains(e.idx());
          Vote vote =
              new GradeOfExecution(e, this, nation.goes.get(e.idx() - 1).floatValue(), review);
          votes.put(vote);
          if (review)
            // save reviews for later
            reviews.add(vote);
        }

        Thread.sleep(rnd.nextInt(1500));
      } while (!Element.END.equals(lastElement));

      // send reviewed elements
      for (Vote v : reviews) {
        Thread.sleep(4000);
        votes.put(v);
      }

      // send the components
      votes.put(new SkatingSkills(this, nation.ss));
      Thread.sleep(rnd.nextInt(500));
      votes.put(new Transitions(this, nation.tr));
      Thread.sleep(rnd.nextInt(500));
      votes.put(new Performance(this, nation.pf));
      Thread.sleep(rnd.nextInt(500));
      votes.put(new Composition(this, nation.cp));
      Thread.sleep(rnd.nextInt(500));
      votes.put(new Interpretation(this, nation.im));
      Thread.sleep(rnd.nextInt(500));

    } catch (InterruptedException e) {
      e.printStackTrace();
      lastElement = Element.END;
    }
  }
}
