package it.unipd.app2020.tictactoe;

import static it.unipd.app2020.tictactoe.Positions.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

enum Positions {
  P1,
  P2,
  P3,
  P4,
  P5,
  P6,
  P7,
  P8,
  P9;

  public String moveName() {
    return this.name().substring(1);
  }
}

enum Player {
  PlayerO,
  PlayerX;
}

public class Game {

  Player current;
  EnumSet<Positions> playerO;
  EnumSet<Positions> playerX;
  GameStatus status;
  int index;

  private static Set<EnumSet<Positions>> winning = new HashSet<>();

  static {
    winning.add(EnumSet.of(P1, P2, P3));
    winning.add(EnumSet.of(P4, P5, P6));
    winning.add(EnumSet.of(P7, P8, P9));
    winning.add(EnumSet.of(P1, P5, P9));
    winning.add(EnumSet.of(P7, P5, P3));
    winning.add(EnumSet.of(P1, P4, P7));
    winning.add(EnumSet.of(P2, P5, P8));
    winning.add(EnumSet.of(P3, P6, P9));
  }

  private boolean isWinning(EnumSet<Positions> test) {
    return winning.stream().anyMatch(w -> test.containsAll(w));
  }

  private Game(Player current, EnumSet<Positions> playerO, EnumSet<Positions> playerX) {
    this.current = current;
    this.playerO = playerO;
    this.playerX = playerX;
    index = playerO.size() + playerX.size();
    if (isWinning(playerO)) status = GameStatus.PLAYER_O_WON;
    else if (isWinning(playerX)) status = GameStatus.PLAYER_X_WON;
    else if (index == 9) status = GameStatus.TIE;
    else status = GameStatus.ONGOING;
  }

  /**
   * Giocatore che deve esprimere la mossa
   *
   * @return
   */
  public Player currentPlayer() {
    return current;
  }

  /**
   * Elenco delle prossime mosse disponibili
   *
   * @return
   */
  public String availableMoves() {
    return List.of(Positions.values()).stream()
        .filter(p -> !playerO.contains(p))
        .filter(p -> !playerX.contains(p))
        .map(p -> p.name().substring(1))
        .collect(Collectors.joining(""));
  }

  private char position(Positions pos) {
    if (playerO.contains(pos)) return 'O';
    else if (playerX.contains(pos)) return 'X';
    else return '_';
  }

  /**
   * Campo di gioco attuale
   *
   * @return
   */
  public String gameString() {

    return """
      %c%c%c
      %c%c%c
      %c%c%c"""
        .formatted(
            position(P1),
            position(P2),
            position(P3),
            position(P4),
            position(P5),
            position(P6),
            position(P7),
            position(P8),
            position(P9));
  }

  /** Risultati dell'esecuzione delle prossime mosse. */
  public List<Game> moves() {
    List<Game> res = new ArrayList<Game>();
    if (status == GameStatus.ONGOING)
      for (Positions p : Positions.values()) {
        var mv = move(p);
        if (mv.isPresent()) res.add(mv.get());
      }
    return res;
  }

  /**
   * Numero di mosse finora fatte
   *
   * @return
   */
  public int index() {
    return index;
  }

  /** Stato corrente del gioco */
  public GameStatus status() {
    return status;
  }

  /**
   * Calcola il risultato della mossa.
   *
   * @return Lo stato dopo l'esecuzione della mossa. Nulla se la mossa non era valida.
   */
  public Optional<Game> move(Positions p) {
    Optional<Game> res = Optional.empty();
    if (!playerO.contains(p) && !playerX.contains(p)) {
      switch (current) {
        case PlayerO:
          var newsetO = EnumSet.copyOf(playerO);
          newsetO.add(p);
          res = Optional.of(new Game(Player.PlayerX, newsetO, EnumSet.copyOf(playerX)));
          break;
        case PlayerX:
          var newsetX = EnumSet.copyOf(playerX);
          newsetX.add(p);
          res = Optional.of(new Game(Player.PlayerO, EnumSet.copyOf(playerO), newsetX));
          break;
      }
    }
    return res;
  }

  /** Prepara un nuovo gioco */
  public static Game setup() {
    return new Game(
        Player.PlayerO, EnumSet.noneOf(Positions.class), EnumSet.noneOf(Positions.class));
  }
}
