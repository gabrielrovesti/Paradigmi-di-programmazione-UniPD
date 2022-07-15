package it.unipd.pdp2021.threads;

import static java.lang.System.out;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/** Run multiple tasks on a fixed thread pool. */
public class FixedThreadPool {

  public static void main(String[] args) {
    Executor executor = Executors.newFixedThreadPool(4);

    Stream<Thread> threads = Stream.generate(new ThreadSupplier());
    out.println("Scheduling runnables");
    threads.limit(10).forEach((r) -> executor.execute(r));
    out.println("Done scheduling.");
  }
}
