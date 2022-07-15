package it.unipd.pdp2021.sockets;

import static it.unipd.pdp2021.sockets.Game.PLAYER_O;
import static it.unipd.pdp2021.sockets.Game.masks;

/** Risultato di una mossa e stato del gioco */
public class GameResult {

  /** prossimo giocatore a muovere, o vincitore se la partita è terminata */
  public final int next;
  /** la partita è valida */
  public final boolean valid;
  /** la partita è terminata */
  public final boolean end;
  /** stato leggibile del gioco */
  public final String board;

  public GameResult(int next, boolean valid, boolean end, int[] board) {
    this.next = next;
    this.valid = valid;
    this.end = end;
    if (!valid) {
      this.board = "";
    } else {
      char[] out = new char[9];
      StringBuffer buf = new StringBuffer();

      for (int i = 0; i < 9; i++)
        if ((board[0] & masks[i]) > 0) {
          out[i] = 'O';
        } else if ((board[1] & masks[i]) > 0) {
          out[i] = 'X';
        } else {
          out[i] = '_';
          buf.append("" + i).append(" ");
        }
      this.board =
          new StringBuffer(
                  out[0] + " " + out[1] + " " + out[2] + "\n" + out[3] + " " + out[4] + " " + out[5]
                      + "\n" + out[6] + " " + out[7] + " " + out[8] + "\n")
              .append(buf)
              .toString()
              .trim();
    }
  }

  @Override
  public String toString() {
    return new StringBuffer(next == PLAYER_O ? "PLAYER_O" : "PLAYER_X")
        .append(" ")
        .append(valid)
        .append(" ")
        .append(end)
        .append("\n")
        .append(board)
        .toString();
  }
}
