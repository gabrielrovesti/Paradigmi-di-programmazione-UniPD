package it.unipd.pdp2021.bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Strikes {

  public static List<Arguments> strikes() {
    return List.of(
        arguments("11X----------------", 12),
        arguments("11X34--------------", 26),
        arguments("11XX34------------", 49),
        arguments("11XX344/X8/4-----", 107));
  }

  @ParameterizedTest
  @MethodSource
  public void strikes(String score, int result) {
    assertEquals(result, new BowlingGame(score).score());
    assertEquals(result, new BowlingGame2(score).score());
  }
}
