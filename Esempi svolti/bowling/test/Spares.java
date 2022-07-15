package it.unipd.pdp2021.bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Spares {

  public static List<Arguments> spares() {
    return List.of(
        arguments("1/------------------", 10),
        arguments("1/34----------------", 20),
        arguments("1/341/341/341/341/34", 100),
        arguments("1/341/341/341/341/22", 96),
        arguments("5/11------------3/11", 26));
  }

  @ParameterizedTest
  @MethodSource
  public void spares(String score, int result) {
    assertEquals(result, new BowlingGame(score).score());
    assertEquals(result, new BowlingGame2(score).score());
  }
}
