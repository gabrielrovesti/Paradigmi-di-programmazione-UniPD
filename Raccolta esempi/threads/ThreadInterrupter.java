package it.unipd.pdp2021.threads;

import static java.lang.System.out;

/**
 * A thread that interrupts another one after one waiting one second, while reporting about the
 * target liveness.
 */
class Interrupter implements Runnable {

  private final Thread tgt;

  Interrupter(Thread tgt) {
    this.tgt = tgt;
  }

  @Override
  public void run() {
    out.println("Target Thread is live: " + tgt.isAlive());
    for (int i = 0; i < 4; i++) {
      try {
        Thread.sleep(1000L);
        out.println("Interrupting target thread");
        tgt.interrupt();
        out.println("Target interrupted.");
      } catch (InterruptedException e) {
        out.println("Interrupter Interrupted");
        e.printStackTrace();
      }
    }
    out.println("Target Thread alive: " + tgt.isAlive());
  }
}

/** A Thread that interrupts another one. */
public class ThreadInterrupter {

  public static void main(String[] args) {

    // A thread that stays alive for a while
    final Thread tgt = new ThreadSupplier(2000L).get();

    // Another thread, that interrupts the target one
    final Thread interrupter = new Thread(new Interrupter(tgt));

    // Start both threads.
    interrupter.start();
    tgt.start();
  }
}
