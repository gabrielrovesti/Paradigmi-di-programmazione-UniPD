package it.unipd.pdp2021.threads;

import static java.lang.System.out;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class FactorialBuilder implements Supplier<Callable<Integer>> {

  Random random = new Random();

  @Override
  public Callable<Integer> get() {
    int number = random.nextInt(8) + 1;
    return () -> {
      int result = 1;
      if ((number == 0) || (number == 1)) {
        result = 1;
      } else {
        for (int i = 2; i <= number; i++) {
          result *= i;
          TimeUnit.MILLISECONDS.sleep(120);
        }
      }
      out.printf("%s: %d! = %d\n", Thread.currentThread().getName(), number, result);

      return result;
    };
  }
}
