package it.unipd.pdp2021.ex1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

interface ScoreRow {
  /** @return The score for the row. 0 if not completed. */
  float partial();

  /** @return The status for the row, if completed */
  ElementScore status();

  /** @return If true, the element has been marked for review */
  boolean review();

  /**
   * Add a vote to the row
   *
   * @param v Vote to add
   * @return Row with the new vote
   */
  ScoreRow add(Vote v);
}

record ElementScoreRow(Optional<Float> bv, Optional<Float>[] goes, boolean review)
    implements ScoreRow {

  public float partial() {
    boolean done = bv.isPresent() && Arrays.stream(goes).allMatch(Optional::isPresent);
    if (done) return ScoreBoard.trimmedAverage(goes) + bv.get();
    else return 0f;
  }

  public ElementScore status() {
    if (review) return ElementScore.Review;

    boolean done = bv.isPresent() && Arrays.stream(goes).allMatch(Optional::isPresent);
    if (done) {
      var score = ScoreBoard.trimmedAverage(goes);
      if (score >= 0f) return ElementScore.Good;
      else return ElementScore.Bad;
    } else return ElementScore.NotDone;
  }

  public ScoreRow add(Vote v) {
    boolean rev = review;
    Optional<Float> base = bv;
    if (v instanceof GradeOfExecution g) {
      goes[g.judge().idx - 1] = Optional.of(g.value());
      rev = g.review();
    } else if (v instanceof BaseValue b) {
      base = Optional.of(b.value());
    }
    return new ElementScoreRow(base, goes, rev);
  }
}

record ComponentScoreRow(Optional<Float>[] comp) implements ScoreRow {

  public float partial() {
    boolean done = Arrays.stream(comp).allMatch(Optional::isPresent);
    if (done) return 2.0f * ScoreBoard.trimmedAverage(comp);
    else return 0f;
  }

  public ElementScore status() {
    return ElementScore.NotDone;
  }

  public boolean review() {
    return false;
  }

  public ScoreRow add(Vote v) {
    if (v instanceof JudgeVote jv) {
      comp[jv.judge().idx - 1] = Optional.of(jv.value());
    }
    return new ComponentScoreRow(comp);
  }
}

record CurrentResult(String elements, float total) {
  static CurrentResult calc(Set<Map.Entry<Element, ScoreRow>> scores) {
    char res[] = new char[scores.size()];
    Arrays.fill(res, ' ');
    float sum = 0;

    for (Map.Entry<Element, ScoreRow> row : scores) {
      if (row.getKey().idx() > 0) {
        res[row.getKey().idx() - 1] = row.getValue().status().repr;
      }
      sum += row.getValue().partial();
    }
    return new CurrentResult(String.valueOf(res), sum);
  }
}

/** Possible Element score flags. */
enum ElementScore {
  NotDone(' '),
  Good('_'),
  Review('!'),
  Bad('X');

  /** Element representation in the element bar */
  final char repr;

  ElementScore(char repr) {
    this.repr = repr;
  }
}

public class ScoreBoard implements Runnable {

  private final BlockingQueue<Vote> votes;
  private boolean stop = false;
  private Thread summing;
  private Map<Element, ScoreRow> map;
  public AtomicReference<CurrentResult> result;

  ScoreBoard(BlockingQueue<Vote> votes) {
    this.votes = votes;
    summing = new Thread(this);
  }

  @SuppressWarnings("unchecked")
  private Optional<Float>[] init() {
    var res = new Optional[Nation.values().length];
    Arrays.fill(res, Optional.empty());
    return res;
  }

  private final Element SKILLS = new Element(0, Element.SKILLS);
  private final Element TRANSITIONS = new Element(0, Element.TRANSITIONS);
  private final Element PERFORMANCE = new Element(0, Element.PERFORMANCE);
  private final Element COMPOSITION = new Element(0, Element.COMPOSITION);
  private final Element INTERPRETATION = new Element(0, Element.INTERPRETATION);

  @Override
  public void run() {
    // setup the data collector
    map = new HashMap<>();

    map.put(SKILLS, new ComponentScoreRow(init()));
    map.put(TRANSITIONS, new ComponentScoreRow(init()));
    map.put(PERFORMANCE, new ComponentScoreRow(init()));
    map.put(COMPOSITION, new ComponentScoreRow(init()));
    map.put(INTERPRETATION, new ComponentScoreRow(init()));

    result = new AtomicReference<CurrentResult>(new CurrentResult("", 0f));
    
    // read a vote, collect and sum
    try {

      while (!stop) {
        Vote vt = votes.poll(1, TimeUnit.SECONDS);
        if (vt != null) {
          if (vt instanceof BaseValue bv)
            map.compute(
                bv.element(),
                (k, v) ->
                    (v == null)
                        ? new ElementScoreRow(Optional.of(bv.value()), init(), false)
                        : v.add(bv));
          else if (vt instanceof GradeOfExecution goe)
            map.compute(
                goe.element(),
                (k, v) ->
                    (v == null)
                        ? new ElementScoreRow(Optional.empty(), init(), false).add(goe)
                        : v.add(goe));
          else if (vt instanceof SkatingSkills ss) map.compute(SKILLS, (k, v) -> v.add(ss));
          else if (vt instanceof Transitions tr) map.compute(TRANSITIONS, (k, v) -> v.add(tr));
          else if (vt instanceof Performance pf) map.compute(PERFORMANCE, (k, v) -> v.add(pf));
          else if (vt instanceof Composition cp) map.compute(COMPOSITION, (k, v) -> v.add(cp));
          else if (vt instanceof Interpretation in)
            map.compute(INTERPRETATION, (k, v) -> v.add(in));

          result.set(CurrentResult.calc(map.entrySet()));
        }
      }
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    this.stop = true;
  }

  public void start() {
    this.summing.start();
  }

  static String format(String elements, float total) {
    return "%s %4.2f".formatted(elements, total);
  }

  static float trimmedAverage(Optional<Float>[] values) {
    int len = values.length;
    float max[] = new float[] {-20.0f};
    float min[] = new float[] {20.0f};
    float sum[] = new float[] {0f};

    for (Optional<Float> f : values)
      f.ifPresent(
          v -> {
            max[0] = v > max[0] ? v : max[0];
            min[0] = v < min[0] ? v : min[0];
            sum[0] += v;
          });

    return (sum[0] - max[0] - min[0]) / (len - 2);
  }
}
