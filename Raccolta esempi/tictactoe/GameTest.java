package it.unipd.pdp2021.tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class GameTest {
  String firstmoves =
      """
        O__ _O_ __O ___ ___ ___ ___ ___ ___
        ___ ___ ___ O__ _O_ __O ___ ___ ___
        ___ ___ ___ ___ ___ ___ O__ _O_ __O""";

  String secondmoves =
      """
        OX_ O_X O__ O__ O__ O__ O__ O__
        ___ ___ X__ _X_ __X ___ ___ ___
        ___ ___ ___ ___ ___ X__ _X_ __X""";

  String thirdmoves =
      """
        OXO OX_ OX_ OX_ OX_ OX_ OX_
        ___ O__ _O_ __O ___ ___ ___
        ___ ___ ___ ___ O__ _O_ __O""";

  /**
   * Suddivide una stringa contenente molti esempi in un elenco di esempi singoli.
   *
   * @param in Stringa complessiva degli esempi
   * @return Elenco di esempi estratti dall'input
   */
  private List<String> splitExamples(String in) {
    List<String> itms = new ArrayList<>();
    for (String s : in.split("\n")) itms.addAll(Arrays.asList(s.split(" ")));

    // ogni esempio è fatto da tre righe
    int step = itms.size() / 3;
    assertEquals(0, itms.size() % 3);

    List<String> res = new ArrayList<>();
    for (int i = 0; i < step; i++)
      res.add(itms.get(i) + "\n" + itms.get(step + i) + "\n" + itms.get(2 * step + i));
    return res;
  }

  @Test
  public void gameSetup() {
    Game game = Game.setup();
    assertEquals(Player.PlayerO, game.currentPlayer());
    assertEquals("123456789", game.availableMoves());
  }

  @Test
  public void moves() {
    Game game = Game.setup();
    assertEquals("""
      ___
      ___
      ___""", game.gameString());
    List<Game> moves = game.moves();
    assertEquals(9, moves.size());
    assertIterableEquals(
        splitExamples(firstmoves),
        moves.stream().map((Game g) -> g.gameString()).collect(Collectors.toList()));
    moves.forEach((g) -> assertEquals(1, g.index()));
    moves.forEach((g) -> assertEquals(GameStatus.ONGOING, g.status()));
    moves.forEach((g) -> assertEquals(Player.PlayerX, g.currentPlayer()));
  }

  @Test
  public void secondMove() {
    Game game = Game.setup();
    assertEquals("""
      ___
      ___
      ___""", game.gameString());
    List<Game> moves = game.moves().get(0).moves();
    assertEquals(8, moves.size());
    assertIterableEquals(
        splitExamples(secondmoves),
        moves.stream()
            .map((Game g) -> g.gameString())
            // .peek(s -> System.out.println("*\n%s".formatted(s)))
            .collect(Collectors.toList()));
    moves.forEach((g) -> assertEquals(2, g.index()));
    moves.forEach((g) -> assertEquals(GameStatus.ONGOING, g.status()));
    moves.forEach((g) -> assertEquals(Player.PlayerO, g.currentPlayer()));
    assertEquals("3456789", moves.get(0).availableMoves());
    assertEquals("2456789", moves.get(1).availableMoves());
  }

  @Test
  public void thirdMove() {
    Game game = Game.setup();
    assertEquals("""
      ___
      ___
      ___""", game.gameString());
    List<Game> moves = game.moves().get(0).moves().get(0).moves();
    assertEquals(7, moves.size());
    assertIterableEquals(
        splitExamples(thirdmoves),
        moves.stream().map((Game g) -> g.gameString()).collect(Collectors.toList()));
    moves.forEach((g) -> assertEquals(3, g.index()));
    moves.forEach((g) -> assertEquals(GameStatus.ONGOING, g.status()));
    moves.forEach((g) -> assertEquals(Player.PlayerX, g.currentPlayer()));
    assertEquals("456789", moves.get(0).availableMoves());
    assertEquals("356789", moves.get(1).availableMoves());
  }

  @Test
  public void wonGame() {
    Game game = Game.setup();
    for (Positions mv :
        List.of(Positions.P1, Positions.P2, Positions.P4, Positions.P3, Positions.P7)) {
      game = game.move(mv).get();
    }

    assertEquals(GameStatus.PLAYER_O_WON, game.status());
    assertEquals("""
      OXX
      O__
      O__""", game.gameString());
  }

  @Test
  public void wonXGame() {
    Game game = Game.setup();
    for (Positions mv :
        List.of(
            Positions.P1, Positions.P2, Positions.P3, Positions.P5, Positions.P4, Positions.P8)) {
      game = game.move(mv).get();
    }

    assertEquals(GameStatus.PLAYER_X_WON, game.status());
    assertEquals("""
      OXO
      OX_
      _X_""", game.gameString());
  }

  @Test
  public void tiedGame() {
    Game game = Game.setup();
    for (Positions mv :
        List.of(
            Positions.P1,
            Positions.P2,
            Positions.P3,
            Positions.P4,
            Positions.P5,
            Positions.P7,
            Positions.P8,
            Positions.P9,
            Positions.P6)) {
      game = game.move(mv).get();
    }

    assertEquals(GameStatus.TIE, game.status());
    assertEquals("""
      OXO
      XOO
      XOX""", game.gameString());
  }

  @Test
  public void lostGame() {
    Game game = Game.setup();
    for (Positions mv :
        List.of(
            Positions.P1,
            Positions.P2,
            Positions.P3,
            Positions.P5,
            Positions.P4,
            Positions.P6,
            Positions.P8,
            Positions.P9,
            Positions.P7)) {
      game = game.move(mv).get();
    }

    assertEquals("""
      OXO
      OXX
      OOX""", game.gameString());
    assertEquals(GameStatus.PLAYER_O_WON, game.status());
  }
}
