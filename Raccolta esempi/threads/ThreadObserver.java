package it.unipd.pdp2021.threads;

import static java.lang.System.out;

/** A Thread that observes another one's liveness. */
public class ThreadObserver {

  public static void main(String[] args) {

    // A thread that stays alive for a while
    final Thread tgt = new ThreadSupplier(800L).get();

    // Another thread, that checks if target is alive
    final Thread observer =
        new Thread(
            () -> {
              out.println("Target Thread is live: " + tgt.isAlive());
              for (int i = 0; i < 10; i++) {
                try {
                  Thread.sleep(100L);
                  out.println("Target Thread is live: " + tgt.isAlive());
                } catch (InterruptedException e) {
                  out.println("Observer Interrupted");
                  e.printStackTrace();
                }
              }
              out.println("Target Thread is live: " + tgt.isAlive());
            });

    // Start both threads.
    observer.start();
    tgt.start();
  }
}
