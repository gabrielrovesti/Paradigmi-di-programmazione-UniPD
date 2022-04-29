package it.unipd.pdp2021.streams;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamCollector {
  public static void main(String[] args) {
    int res = IntStream.range(1, 1001).boxed().parallel().collect(Collectors.summingInt((i) -> i));
    System.out.println(">>>> " + res);
  }
}
