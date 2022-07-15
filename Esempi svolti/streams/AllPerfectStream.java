package it.unipd.pdp2021.streams;

import java.util.stream.IntStream;

public class AllPerfectStream {
  public static void main(String[] args) {
    IntStream.range(10, 10000)
        .boxed()
        .parallel()
        .map(new Divisors())
        .filter(new Perfect())
        .limit(2)
        .forEach(
            (CandidateNumber c) -> {
              System.out.println(">>> " + c.toString());
            });
  }
}
