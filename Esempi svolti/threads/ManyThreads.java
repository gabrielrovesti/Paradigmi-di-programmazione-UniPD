package it.unipd.pdp2021.threads;

import static java.lang.System.out;

import java.util.stream.Stream;

public class ManyThreads {

  public static void main(String[] args) {
    Stream<Thread> threads = Stream.generate(new ThreadSupplier());

    out.println("Starting Threads");
    threads.limit(10).forEach((Thread a) -> a.start());
    out.println("Done starting.");
  }
}
