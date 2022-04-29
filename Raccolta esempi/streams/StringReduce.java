package it.unipd.pdp2021.streams;

import java.util.stream.IntStream;

public class StringReduce {
  public static void main(String[] args) {
    String res =
        IntStream.range(1, 10001)
            .boxed()
            .map((i) -> i.toString())
            .parallel()
            .reduce("", String::concat);
    System.out.println(">>>> " + res);
  }
}
