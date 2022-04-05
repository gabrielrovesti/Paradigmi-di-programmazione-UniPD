package it.unipd.pdp2021;

import static it.unipd.pdp2021.Pair.pair;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class Pair {
  public final int a, b;

  Pair(int a, int b) {
    this.a = a;
    this.b = b;
  }

  static Pair pair(int a, int b) {
    return new Pair(a, b);
  }
}

public class FibonacciTest {

  @Test
  public void fibonacciTest() {
    assertEquals(0, Fibonacci.fibonacci(0));
    assertEquals(1, Fibonacci.fibonacci(1));
    assertEquals(1, Fibonacci.fibonacci(2));
    assertEquals(2, Fibonacci.fibonacci(3));
    assertEquals(3, Fibonacci.fibonacci(4));
    assertEquals(5, Fibonacci.fibonacci(5));
    assertEquals(8, Fibonacci.fibonacci(6));
    assertEquals(13, Fibonacci.fibonacci(7));
    assertEquals(21, Fibonacci.fibonacci(8));
    assertEquals(34, Fibonacci.fibonacci(9));
  }

  @Test
  public void switchTest() {
    int[] src = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    int[] res = {0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55};

    for (int i = 0; i < src.length; i++) assertEquals(res[i], Fibonacci.fibonacciSwitch(src[i]));
  }

  public static List<Pair> memoTest() {
    return List.of(
        pair(0, 0),
        pair(1, 1),
        pair(2, 1),
        pair(3, 2),
        pair(4, 3),
        pair(5, 5),
        pair(6, 8),
        pair(7, 13),
        pair(8, 21),
        pair(9, 34));
  }

  @ParameterizedTest
  @MethodSource
  public void memoTest(Pair input) {
    Fibonacci fib = new Fibonacci();
    assertEquals(input.b, fib.fibonacciMemo(input.a));
  }
}
