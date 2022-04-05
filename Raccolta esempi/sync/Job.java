package it.unipd.pdp2021.sync;

import it.unipd.pdp2021.safe.Printer;

class Job implements Runnable {

  private Printer printQueue;

  public Job(Printer printQueue) {
    this.printQueue = printQueue;
  }

  @Override
  public void run() {
    System.out.printf("%s: Going to print a document\n", Thread.currentThread().getName());
    printQueue.printJob(new Object());
    System.out.printf("%s: The document has been printed\n", Thread.currentThread().getName());
  }
}
