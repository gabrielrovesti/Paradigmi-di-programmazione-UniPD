package it.unipd.pdp2021.streams;

import java.util.stream.IntStream;

public class CountStream {

  public static void main(String[] args) {
    long cnt =
        IntStream.range(1, 2000)
            .parallel()
            .map(
                (i) -> {
                  System.out.println(i);
                  return i * 2;
                })
            .limit(5)
            .count();
    System.out.println(">" + cnt);
  }
}
