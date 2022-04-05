package it.unipd.pdp2021.safe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

/** A concurrent printer server that consumes jobs with many printers. */
class ConcurrentPrinter implements Printer {

  static final int QUEUE_SIZE = 10;

  // the printer pool size: how many printers to drive
  private int size;
  // the job queue
  private LinkedBlockingQueue<PrintJob> queue;
  // the executor to run the print jobs
  private ExecutorService executor;
  // read the runtime value of available processor cores
  private final int cores = Runtime.getRuntime().availableProcessors();

  ConcurrentPrinter(int printers) {
    // limit printers to effective cores
    size = printers < cores ? printers : cores;
    // size and build the queue
    queue = new LinkedBlockingQueue<PrintJob>(QUEUE_SIZE);
    // start the executor
    executor = Executors.newFixedThreadPool(size);
    // start drivers
    IntStream.range(0, size).forEach((a) -> executor.execute(new PrinterDriver(queue)));
  }

  @Override
  public void printJob(Object document) {
    try {
      queue.put(new PrintJob(document));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  void shutdown() {
    executor.shutdown();
  }
}
