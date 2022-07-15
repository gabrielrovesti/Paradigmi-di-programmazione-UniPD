package it.unipd.app2020.tictactoe;

import java.util.ArrayList;
import java.util.List;

class CountStatus {
  public final List<Game> games;
  public final int wonO, wonX, ties;

  CountStatus(List<Game> games) {
    this.games = games;
    wonO = 0;
    wonX = 0;
    ties = 0;
  }

  CountStatus(List<Game> games, int wonO, int wonX, int ties) {
    this.games = games;
    this.wonO = wonO;
    this.wonX = wonX;
    this.ties = ties;
  }

  @Override
  public String toString() {
    return "%d O/X/= %d/%d/%d".formatted(games.size(), wonO, wonX, ties);
  }
}

/** Count all possible solutions running breadth-first. */
public class BreadthFirstCounter {
  public static void main(String[] args) {

    long start = System.currentTimeMillis();

    Game root = Game.setup();
    List<Game> first = root.moves();
    // System.out.println("first: " + first.size());

    List<Game> second = breadth(first);
    // System.out.println("second: " + second.size());

    List<Game> third = breadth(second);
    // System.out.println("third: " + third.size());

    List<Game> fourth = breadth(third);
    // System.out.println("fourth: " + fourth.size());

    CountStatus fifth = breadth(new CountStatus(fourth));
    // System.out.println("fifth: " + fifth.toString());

    CountStatus sixth = breadth(fifth);
    // System.out.println("sixth: " + sixth.toString());

    CountStatus seventh = breadth(sixth);
    // System.out.println("seventh: " + seventh.toString());

    CountStatus eighth = breadth(seventh);
    // System.out.println("eighth: " + eighth.toString());

    CountStatus ninth = breadth(eighth);
    long end = System.currentTimeMillis();

    System.out.println(
        "Won O: %d Won X: %d Tied: %d (%d)"
            .formatted(ninth.wonO, ninth.wonX, ninth.ties, (end - start)));
  }

  private static List<Game> breadth(List<Game> games) {
    List<Game> res = new ArrayList<>();
    for (Game g : games) res.addAll(g.moves());
    return res;
  }

  private static CountStatus breadth(CountStatus status) {
    int wO = 0, wX = 0, ts = 0;
    List<Game> res = new ArrayList<>();
    for (Game g : breadth(status.games))
      switch (g.status) {
        case PLAYER_O_WON:
          wO++;
          break;
        case PLAYER_X_WON:
          wX++;
          break;
        case TIE:
          ts++;
          break;
        case ONGOING:
          res.add(g);
      }
    return new CountStatus(res, wO + status.wonO, wX + status.wonX, ts + status.ties);
  }
}
