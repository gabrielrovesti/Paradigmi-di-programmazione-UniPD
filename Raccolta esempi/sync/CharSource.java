package it.unipd.pdp2021.sync;

import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

/** Bounded, non thread-safe random source of characters */
class CharSource {
  private final String[] content;
  private int index = 0;

  /**
   * Build the data
   *
   * @param lines number of lines to build
   * @param length max length of any line
   */
  public CharSource(int lines, int length) {
    Random random = new Random();
    content = new String[lines];
    IntStream.range(0, lines)
        .forEach(
            (i) -> {
              StringBuffer target = new StringBuffer(length);
              IntStream.range(0, random.nextInt(length) + 1)
                  .forEach((j) -> target.append('0' + random.nextInt('z' - '0')));
              content[i] = target.toString();
            });
  }

  /**
   * More lines are available
   *
   * @return true if there are more lines
   */
  public boolean hasMoreLines() {
    return index < content.length;
  }

  /**
   * Return a line.
   *
   * @return A line, or Empty if there aren't.
   */
  public Optional<String> getLine() {
    if (this.hasMoreLines()) {
      System.out.println("Source: " + (content.length - index));
      return Optional.of(content[index++]);
    }
    return Optional.empty();
  }
}
