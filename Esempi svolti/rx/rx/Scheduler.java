package it.unipd.pdp2021.rx;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Scheduler {
  static final boolean[] done = new boolean[] {false};

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Defining...");
    Observable.range(0, 1000000)
        .map(new RxDivisors())
        .filter(new RxPerfectPredicate())
        .subscribeOn(Schedulers.computation())
        .subscribe(
            (c) -> {
              System.out.println(c);
            },
            (t) -> {
              t.printStackTrace();
            },
            () -> {
              System.out.println("Done");
              done[0] = true;
            });
    System.out.println("Defined");
    while (!done[0]) Thread.sleep(1000);
    System.out.println("End");
  }
}
