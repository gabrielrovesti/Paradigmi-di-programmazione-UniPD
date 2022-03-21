package it.unipd.app2020.tictactoe;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

class Score {
  int wonX = 0;
  int wonO = 0;
  int ties = 0;

  Score(int wonX, int wonO, int ties) {
    this.wonO = wonO;
    this.wonX = wonX;
    this.ties = ties;
  }

  static Score score(Game game) {
    return switch (game.status()) {
      case PLAYER_O_WON -> Score.WON_O;
      case PLAYER_X_WON -> Score.WON_X;
      case TIE -> Score.TIE;
      case ONGOING -> Score.ONGOING;
    };
  }

  Score sumOther(Score other) {
    this.wonO += other.wonO;
    this.wonX += other.wonX;
    this.ties += other.ties;
    return this;
  }

  static Score sum(Score a, Score b) {
    return new Score(a.wonX + b.wonX, a.wonO + b.wonO, a.ties + b.ties);
    // return a.sumOther(b);
  }

  static Score ONGOING = new Score(0, 0, 0);
  static Score WON_X = new Score(1, 0, 0);
  static Score WON_O = new Score(0, 1, 0);
  static Score TIE = new Score(0, 0, 1);
}

public class IteratorCounter {
  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    Score res =
        StreamSupport.stream(new GameIterator(Game.setup()), true)
            .parallel()
            .filter(g -> g.status() != GameStatus.ONGOING)
            .collect(Collectors.reducing(new Score(0, 0, 0), Score::score, Score::sum));
    long end = System.currentTimeMillis();
    System.out.println(
        "Won O: %d  Won X: %d Ties: %d (%d)"
            .formatted(res.wonO, res.wonX, res.ties, (end - start)));
  }
}
