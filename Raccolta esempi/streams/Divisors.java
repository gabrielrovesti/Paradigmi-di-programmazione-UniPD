package it.unipd.pdp2021.streams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/** A Function that transforms an number in a CandidateNumber */
public class Divisors implements Function<Integer, CandidateNumber> {

  @Override
  public CandidateNumber apply(Integer n) {
    List<Integer> divs = new ArrayList<Integer>();

    if (n == 2) {
      divs.add(2);
      return new CandidateNumber(2, divs);
    }

    int i = 2;
    long limit = Math.round(Math.sqrt(Math.abs(n)));

    while (i <= limit) {
      if (n % i == 0) {
        divs.add(i);
        if (i != (n / i)) divs.add(n / i);
      }
      i++;
    }
    Collections.sort(divs);
    return new CandidateNumber(n, divs);
  }
}
