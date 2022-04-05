package it.unipd.pdp2021.safe;

/** Add to a shared counter with many threads concurrently (and probably fail) */
public class Adder {

  int target = 0;

  /** Add to the target variable concurrently */
  public void add() {
    // we repeat the same code to be sure to close over the shared variable
    Thread t1 =
        new Thread(
            () -> {
              for (int i = 0; i < 100000; i++) {
                target += 1;
              }
            });
    Thread t2 =
        new Thread(
            () -> {
              for (int i = 0; i < 100000; i++) {
                target += 1;
              }
            });
    Thread t3 =
        new Thread(
            () -> {
              for (int i = 0; i < 100000; i++) {
                target += 1;
              }
            });
    t1.start();
    t2.start();
    t3.start();
  }
}
