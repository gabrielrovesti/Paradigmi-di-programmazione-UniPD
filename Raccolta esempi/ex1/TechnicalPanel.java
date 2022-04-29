package it.unipd.pdp2021.ex1;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/** The tecnical panel watches the elements and emits their base values. */
public class TechnicalPanel implements Runnable {

  private float baseValues[] =
      new float[] {
        10.50f, 10.30f, 5.30f, 3.50f, 3.30f, 16.28f, 7.93f, 14.74f, 5.61f, 6.60f, 3.00f, 2.00f,
        3.50f
      };
  private final BlockingQueue<Element> screen;
  private final BlockingQueue<Vote> values;
  private final Thread voting;

  public TechnicalPanel(BlockingQueue<Element> screen, BlockingQueue<Vote> values) {
    this.screen = screen;
    this.values = values;
    this.voting = new Thread(this);
  }

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
    int idx = 0;

    try {
      // look at elements, return base values
      do {
        Element e = screen.take();
        lastElement = e.element();
        if (!Element.END.equals(e.element()) && idx < baseValues.length) {
          Vote vote = new BaseValue(e, baseValues[idx++]);
          values.put(vote);
        }

        Thread.sleep(rnd.nextInt(2500));
      } while (!Element.END.equals(lastElement));

    } catch (InterruptedException e) {
      e.printStackTrace();
      lastElement = Element.END;
    }
  }
}
