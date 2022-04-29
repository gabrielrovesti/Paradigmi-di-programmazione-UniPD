package it.unipd.pdp2021.streams;

import java.util.stream.IntStream;

public class OrderedStream {
  public static void main(String[] args) {
    IntStream.range(1, 20)
        .parallel()
        .map(
            (i) -> {
              System.out.println(i + " " + Thread.currentThread().getName());
              return i * 2;
            })
        .forEachOrdered(
            (i) -> {
              System.out.println(">>> " + i);
            });
  }
}
