package it.unipd.pdp2021.sync;

/** Tries to perform on both named resources. */
class Waiter implements Runnable {
  private final Named first, second;

  Waiter(Named first, Named second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public void run() {
    String thread = Thread.currentThread().getName();
    System.out.println(thread + " waiting on " + first.name);
    String doing = first.name;
    try {
      first.perform();
      System.out.println(thread + " signalled on " + first.name);
      System.out.println(thread + " waiting on " + second.name);
      doing = second.name;
      second.perform();
    } catch (InterruptedException e) {
      System.out.println(thread + " interrupted on " + doing);
    }
    System.out.println(thread + " signalled on " + second.name);
  }
}
