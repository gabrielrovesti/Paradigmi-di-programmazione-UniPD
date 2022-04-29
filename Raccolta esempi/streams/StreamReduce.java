package it.unipd.pdp2021.streams;

import java.util.stream.IntStream;

public class StreamReduce {
  public static void main(String[] args) {
    int res =
        IntStream.range(1, 1001)
            .parallel()
            .reduce(
                0,
                (a, b) -> {
                  System.out.println(
                      a + "+" + b + "=" + (a + b) + " " + Thread.currentThread().getName());
                  return a + b;
                });
    System.out.println(">>>> " + res);
  }
}
