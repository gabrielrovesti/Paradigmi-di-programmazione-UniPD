package it.unipd.pdp2021.sync;

/** Runso some waiters on some named resources. */
public class NamedWaiter {
  public static void main(String[] args) {
    Named a = new Named("a"), b = new Named("b"), c = new Named("c"), d = new Named("d");

    new Thread(new Waiter(a, d), "Waiter 1").start();
    new Thread(new Waiter(b, d), "Waiter 2").start();
    new Thread(new Waiter(c, d), "Waiter 3").start();

    try {
      System.out.println("Main signalling a");
      a.perform();
      System.out.println("Main signalling b");
      b.perform();
      System.out.println("Main signalling c");
      c.perform();
      System.out.println("Main signalling d");
      d.perform();
      System.out.println("Main done");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
