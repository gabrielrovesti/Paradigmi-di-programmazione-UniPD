package it.unipd.pdp2021.sync;

import it.unipd.pdp2021.safe.Printer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/** A driver of many printers, using semaphores. */
class MultiPrintQueue implements Printer {
  private Semaphore semaphore;
  private boolean[] freePrinters;
  private ReentrantLock lockPrinters;

  public MultiPrintQueue() {
    semaphore = new Semaphore(3);
    freePrinters = new boolean[] {true, true, true};
    lockPrinters = new ReentrantLock();
  }

  /**
   * Print a job
   *
   * @param document Document to print
   */
  public void printJob(Object document) {
    try {
      semaphore.acquire();
      int assignedPrinter = getPrinter();
      Long duration = (long) (Math.random() * 10000);
      System.out.printf(
          "%s: PrintQueue: Printing a Job in Printer%d during %d millis\n",
          Thread.currentThread().getName(), assignedPrinter, duration);
      TimeUnit.MILLISECONDS.sleep(duration);
      System.out.printf(
          "%s: PrintQueue: Done Job in Printer%d\n",
          Thread.currentThread().getName(), assignedPrinter);
      freePrinters[assignedPrinter] = true;
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      semaphore.release();
    }
  }

  int getPrinter() {
    int res = -1;
    try {
      lockPrinters.lock();
      for (int i = 0; i < freePrinters.length; i++) {
        if (freePrinters[i]) {
          res = i;
          freePrinters[i] = false;
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      lockPrinters.unlock();
    }
    return res;
  }
}
