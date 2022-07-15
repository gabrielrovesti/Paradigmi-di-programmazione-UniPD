package it.unipd.pdp2021.web;

import it.unipd.pdp2021.sockets.Game;
import it.unipd.pdp2021.sockets.GameResult;
import java.util.Optional;

/** A game that manages the players arrival */
class NetGame {
  String[] playerId = new String[2];
  private Optional<Game> game = Optional.empty();

  private NetGame(String player1) {
    playerId[0] = player1;
  }

  private NetGame(String player1, String player2) {
    playerId[0] = player1;
    playerId[1] = player2;
    game = Optional.of(new Game());
  }

  boolean started() {
    return game.isPresent();
  }

  GameResult status() {
    return game.get().status();
  }

  GameResult move(int player, int move) {
    return game.get().move(player, move);
  }

  /**
   * Add the second player
   *
   * @param player2 Name of the second player
   * @return A started game
   */
  NetGame match(String player2) {
    return new NetGame(playerId[0], player2);
  }

  /**
   * Start a game by giving the first player.
   *
   * @param player1 Name of the first player
   * @return A game with that is waiting for another player
   */
  static NetGame open(String player1) {
    return new NetGame(player1);
  }
}
