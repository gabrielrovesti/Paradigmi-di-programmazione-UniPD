package it.unipd.pdp2021.sync;

/** A string value that can coordinate access to itself. */
public class Named {
  public final String name;
  private boolean red = false;

  Named(String name) {
    this.name = name;
  }

  /**
   * If available, set the red flag and wait for someone to unblock it. Else, notify one threads
   * that is waiting.
   *
   * @throws InterruptedException
   */
  synchronized void perform() throws InterruptedException {
    if (!red) {
      red = true;
      System.out.println("Named " + name + " " + red + " " + Thread.currentThread().getName());
      this.wait();
    } else {
      red = false;
      System.out.println("Named " + name + " " + red + " " + Thread.currentThread().getName());
      this.notify();
    }
  }
}
