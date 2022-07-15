package it.unipd.pdp2021.bowling;

import java.util.ArrayList;
import java.util.List;

public class BowlingGame1 {

  private List<Integer> scores;

  public BowlingGame1(String frames) {
    this.scores = BowlingGame1.read(frames);
  }

  public int score() {
    int frameIdx = 0;
    int frameCount = 0;
    int res = 0;
    while (frameCount <= 8) {
      res += frameScore(scores.get(frameIdx), scores.get(frameIdx + 1), scores.get(frameIdx + 2));
      if (scores.get(frameIdx) == 10) frameIdx++;
      else frameIdx += 2;
      frameCount++;
    }
    if (scores.get(frameIdx) == 10 || scores.get(frameIdx) + scores.get(frameIdx + 1) == 10)
      res += frameScore(scores.get(frameIdx), scores.get(frameIdx + 1), scores.get(frameIdx + 2));
    else res += frameScore(scores.get(frameIdx), scores.get(frameIdx + 1), 0);
    return res;
  }

  private int frameScore(int a, int b, int c) {
    if (a + b < 10) return a + b;
    else return a + b + c;
  }

  public static List<Integer> read(String score) {

    List<Integer> res = new ArrayList<>();

    String scr = score.replace("-", "0");
    res.add(convert('0', scr.charAt(0)));

    for (int i = 1; i < scr.length(); i++) res.add(convert(scr.charAt(i - 1), scr.charAt(i)));

    return res;
  }

  private static int convert(char previous, char current) {

    if (current == '/') return 10 - (previous - '0');
    else if (current == 'X') return 10;
    else return (current - '0');
  }
}
