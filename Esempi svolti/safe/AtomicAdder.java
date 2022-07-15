package it.unipd.pdp2021.safe;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicAdder {

  public AtomicInteger target = new AtomicInteger(0);

  public void add() {

    Thread t1 =
        new Thread(
            () -> {
              for (int i = 0; i < 100000; i++) {
                target.incrementAndGet();
              }
            });
    Thread t2 =
        new Thread(
            () -> {
              for (int i = 0; i < 100000; i++) {
                target.incrementAndGet();
              }
            });
    Thread t3 =
        new Thread(
            () -> {
              for (int i = 0; i < 100000; i++) {
                target.incrementAndGet();
              }
            });
    t1.start();
    t2.start();
    t3.start();
  }
}
