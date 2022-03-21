package it.unipd.app2020;

import java.util.HashMap;
import java.util.Map;

public class Fibonacci {

  static int fibonacciSwitch(int n) {
    return switch (n) {
      case 0 -> 0;
      case 1 -> 1;
      default -> fibonacciSwitch(n - 2) + fibonacciSwitch(n - 1);
    };
  }

  static int fibonacci(int n) {
    if (n == 0) return 0;
    else if (n == 1) return 1;
    else return fibonacci(n - 2) + fibonacci(n - 1);
  }

  private Map<Integer, Integer> memo = new HashMap<>();

  public int fibonacciMemo(int i) {
    return memo.computeIfAbsent(i, Fibonacci::fibonacci);
  }
}
