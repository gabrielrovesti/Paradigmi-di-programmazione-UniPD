package it.unipd.pdp2021.bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Games {

  public static List<Arguments> games() {
    return List.of(
        arguments("1/35XXX458/X3/23", 160),
        arguments("1/35XXX458/X3/XX6", 189),
        arguments("1/35XXX458/X357/5", 164),
        arguments("1/35XXX458/X3/X43", 180),
        arguments("XXXXXXXXXXXX", 300),
        arguments("XXXXXXXXXX12", 274),
        arguments("1/35XXX458/X3/123", 157),
        arguments("5/5/5/5/5/5/5/5/5/5/5", 150),
        arguments("9-8/--9-9-9-9-9-9-9/1", 84),
        arguments("9-X8-9-9-9-9-9-9-X23", 104));
  }

  @ParameterizedTest(name = "{index}: {0} -> {1}")
  @MethodSource
  public void games(String score, int result) {
    assertEquals(result, new BowlingGame(score).score(), "%s".formatted(score));
    assertEquals(result, new BowlingGame2(score).score(), "%s".formatted(score));
  }
}
