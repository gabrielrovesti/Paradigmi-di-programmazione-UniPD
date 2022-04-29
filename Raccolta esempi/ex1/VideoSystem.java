package it.unipd.pdp2021.ex1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The video system will relay each element to all attached screens.
 *
 * <p>It will stop when it sees the last element.
 */
public class VideoSystem {
  private List<BlockingQueue<Element>> screens = new ArrayList<>();
  Thread record;
  /**
   * A new video system for this rink
   *
   * @param rink Rink to observe
   */
  VideoSystem(BlockingQueue<Element> rink) {
    record =
    new Thread(
        () -> {
          String lastElement = "";
          try {
            do {
              // TODO: Take one element. Send it to all screens.
               var e = rink.take();
               for(BlockingQueue<Element> screen: screens) {
            	   screen.add(e);
               }
               lastElement = e.element();
            } while (!Element.END.equals(lastElement));
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        });
  }

  /**
   * Attach a new screen to the system.
   *
   * <p>Do not call after start();
   *
   * @return A new screen that will receive the recorded elements.
   */
  public BlockingQueue<Element> screen() {
    var res = new LinkedBlockingQueue<Element>();
    screens.add(res);
    return res;
  }

  /** Start recording */
  public void start() {
    record.start();
  }

  /** Call to check if the exercise is over */
  boolean done() {
    return !record.isAlive();
  }
}