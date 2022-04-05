package it.unipd.pdp2021.safe;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/** Represents a driver that executes jobs on one printer. */
class PrinterDriver implements Runnable {

  private final BlockingQueue<PrintJob> queue;
  private final Random rnd;

  PrinterDriver(BlockingQueue<PrintJob> queue) {
    this.queue = queue;
    rnd = new Random();
  }

  @Override
  public void run() {
    try {
      while (true) {
        PrintJob job = queue.take();
        System.out.printf(
            "%s: Took a document that waited %d\n",
            Thread.currentThread().getName(), System.currentTimeMillis() - job.enqueued.getTime());
        int duration = rnd.nextInt(2500);
        // Attenzione: usare TimeUnit.X.wait genera un'eccezione: quale e perché?
        Thread.sleep(duration);
        System.out.printf(
            "%s: Printed a document in %d\n", Thread.currentThread().getName(), duration);
      }
    } catch (InterruptedException ex) {
      System.out.println("Printer shutting down.");
    }
  }
}
