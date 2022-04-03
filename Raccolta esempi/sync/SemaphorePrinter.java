package it.unipd.pdp2021.sync;

/** Semaphore-based printer queue. */
public class SemaphorePrinter {

  public static void main(String args[]) {
    MultiPrintQueue printQueue = new MultiPrintQueue();
    Thread thread[] = new Thread[10];
    for (int i = 0; i < 10; i++) {
      thread[i] = new Thread(new Job(printQueue), "Thread " + i);
    }
    for (int i = 0; i < 10; i++) {
      thread[i].start();
    }
  }
}
