package it.unipd.pdp2021.streams;

import java.util.Optional;
import java.util.stream.IntStream;

public class PerfectStream {
  public static void main(String[] args) {

    var base =
        IntStream.range(10, 100000)
            .boxed()
            .parallel()
            .map(new Divisors())
            .map(
                (CandidateNumber c) -> {
                  // System.out.println(c.toString());
                  return c;
                })
            .filter(new Perfect());

    Optional<CandidateNumber> any = base.findAny();
    System.out.println(any);

    // List<CandidateNumber> match = base.collect(Collectors.toList());
    // try removing .findAny().stream()

    /*
    match.forEach(
        x -> {
          System.out.println(x);
        });
        */
  }
}
