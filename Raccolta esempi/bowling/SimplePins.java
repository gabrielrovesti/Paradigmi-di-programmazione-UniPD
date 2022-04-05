package it.unipd.pdp2021.bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SimplePins {

  public static List<Arguments> simplePins() {
    return List.of(
        arguments("--------------------", 0),
        arguments("1234----------------", 10),
        arguments("12341234123412341234", 50),
        arguments("22222222222222222222", 40));
  }

  @ParameterizedTest
  @MethodSource
  public void simplePins(String score, int result) {
    assertEquals(result, new BowlingGame(score).score());
    assertEquals(result, new BowlingGame2(score).score());
  }
}
