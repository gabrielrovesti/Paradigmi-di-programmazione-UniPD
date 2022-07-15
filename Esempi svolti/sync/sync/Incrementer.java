package it.unipd.pdp2021.sync;

import java.util.concurrent.Callable;
import java.util.stream.IntStream;

/** Increments a counter. */
class Incrementer implements Callable<Boolean> {

  private SimpleCounter counter;

  Incrementer(SimpleCounter counter) {
    this.counter = counter;
  }

  @Override
  public Boolean call() {
    IntStream.range(0, 10).forEach((i) -> counter.add());
    return true;
  }
}
