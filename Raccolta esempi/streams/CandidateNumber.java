package it.unipd.pdp2021.streams;

import java.util.List;
import java.util.StringJoiner;

/** A number that may be prime, or not. */
public class CandidateNumber {
  public final int n;
  public final List<Integer> divisors;

  CandidateNumber(int n, List<Integer> divisors) {
    this.n = n;
    this.divisors = divisors;
  }

  @Override
  public String toString() {
    StringJoiner join = new StringJoiner(",");
    for (Integer i : divisors) join.add(i.toString());
    return n + " " + join.toString();
  }
}
