package it.unipd.pdp2021.safe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/** Runs threads that read a threadlocal variable. */
public class LocalMain {

  public static void main(String[] args) {
    ExecutorService executor = Executors.newFixedThreadPool(4);
    LocalVar var = new LocalVar();
    IntStream.range(0, 20).forEach((a) -> executor.execute(new LocalReader(var, a)));
    executor.shutdown();
  }
}
