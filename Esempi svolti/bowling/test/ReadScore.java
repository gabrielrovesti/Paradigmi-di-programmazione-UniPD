package it.unipd.pdp2021.bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ReadScore {

  public static List<Arguments> readScore() {
    return List.of(
        arguments(
            "--------------------",
            List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
        arguments(
            "1234----------------",
            List.of(1, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
        arguments(
            "12345/--------------",
            List.of(1, 2, 3, 4, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
        arguments(
            "1234-/--------------",
            List.of(1, 2, 3, 4, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
        arguments(
            "12345/XX12345-----", List.of(1, 2, 3, 4, 5, 5, 10, 10, 1, 2, 3, 4, 5, 0, 0, 0, 0, 0)),
        arguments(
            "12345/XX1234512/XX4",
            List.of(1, 2, 3, 4, 5, 5, 10, 10, 1, 2, 3, 4, 5, 1, 2, 8, 10, 10, 4)));
  }

  // https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests
  @ParameterizedTest
  @MethodSource
  public void readScore(String score, List<Integer> result) {
    assertEquals(result, BowlingGame.read(score));
  }
}
