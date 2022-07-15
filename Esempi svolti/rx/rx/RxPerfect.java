package it.unipd.pdp2021.rx;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import it.unipd.pdp2021.streams.CandidateNumber;
import it.unipd.pdp2021.streams.Divisors;
import it.unipd.pdp2021.streams.Perfect;

class RxDivisors implements Function<Integer, CandidateNumber> {

  private final Divisors delegate = new Divisors();

  @Override
  public CandidateNumber apply(Integer i) {
    return delegate.apply(i);
  }
}

class RxPerfectPredicate implements Predicate<CandidateNumber> {
  private final Perfect delegate = new Perfect();

  @Override
  public boolean test(CandidateNumber i) {
    boolean result = delegate.test(i);
    if (result) System.out.println("  testing: " + Thread.currentThread().getName());
    return result;
  }
}

public class RxPerfect {
  public static void main(String[] args) {
    System.out.println("Defining...");
    Observable.range(0, 10000)
        .map(new RxDivisors())
        .filter(new RxPerfectPredicate())
        .subscribe(
            (c) -> {
              System.out.println(c);
            },
            (t) -> {
              t.printStackTrace();
            },
            () -> {
              System.out.println("Done");
            });
    System.out.println("Defined");
  }
}
