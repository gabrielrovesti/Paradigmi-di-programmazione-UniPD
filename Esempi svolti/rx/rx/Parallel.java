package it.unipd.pdp2021.rx;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Parallel {
  static final boolean[] done = new boolean[] {false};

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Defining...");
    long start = System.currentTimeMillis();
    Flowable.range(0, 1000000)
        .parallel()
        .runOn(Schedulers.computation())
        .map(new RxDivisors())
        .filter(new RxPerfectPredicate())
        .sequential()
        .subscribe(
            (c) -> {
              System.out.println(Thread.currentThread().getName() + " " + c);
            },
            (t) -> {
              t.printStackTrace();
            },
            () -> {
              System.out.println("Done in " + (System.currentTimeMillis() - start));
              done[0] = true;
            });
    System.out.println("Defined");
    while (!done[0]) Thread.sleep(1000);
    System.out.println("End");
  }
}
