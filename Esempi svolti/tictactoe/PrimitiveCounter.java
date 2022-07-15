package it.unipd.app2020.tictactoe;

import java.util.stream.StreamSupport;

public class PrimitiveCounter {
  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    long res =
        StreamSupport.stream(new GameIterator(Game.setup()), true)
            .filter(g -> g.status() != GameStatus.ONGOING)
            .parallel()
            .mapToLong(
                g ->
                    switch (g.status()) {
                      case PLAYER_O_WON -> 0x1L;
                      case PLAYER_X_WON -> 0x100000L;
                      case TIE -> 0x10000000000L;
                      case ONGOING -> 0x0L;
                    })
            .sum();
    long end = System.currentTimeMillis();
    System.out.println(
        "Won O: %d  Won X: %d Ties: %d (%d)"
            .formatted(res & 0xfffff, (res >> 20) & 0xfffff, (res >> 40) & 0xfffff, (end - start)));
  }
}
