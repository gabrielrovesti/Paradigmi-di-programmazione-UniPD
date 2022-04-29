package it.unipd.pdp2021.streams;

import java.util.function.Predicate;
import java.util.stream.Collectors;

/** This predicate returns true if a CandidateNumber is a perfect number. */
public class Perfect implements Predicate<CandidateNumber> {

  @Override
  public boolean test(CandidateNumber c) {
    Integer sum = c.divisors.stream().collect(Collectors.summingInt((Integer n) -> n));
    return (sum + 1) == c.n;
  }
}
