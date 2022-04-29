package it.unipd.pdp2021.threads;

import static java.lang.System.out;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/** Run multiple tasks on a pool containing a single thread. */
public class SingleThreadPool {

  public static void main(String[] args) {
    Executor executor = Executors.newSingleThreadExecutor();

    Stream<Thread> threads = Stream.generate(new ThreadSupplier());
    out.println("Scheduling runnables");
    threads.limit(10).forEach((r) -> executor.execute(r));
    out.println("Done scheduling.");
  }
}
