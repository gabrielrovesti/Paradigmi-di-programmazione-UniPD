package it.unipd.pdp2021.mnaut;

/** Modella una partita a tic-tac-toe */
public class Game {

  /** bitmask per le posizioni nel campo */
  static final int[] masks = new int[] {0x1, 0x2, 0x4, 0x8, 0x10, 0x20, 0x40, 0x80, 0x100};

  static final int TIED = 0x1ff;

  public static int PLAYER_O = 0;
  public static int PLAYER_X = 1;

  // la partita è valida
  private boolean valid;
  // prossimo giocatore
  private int next;
  // caselle segnate da ciascun giocatore
  private int[] board = new int[2];

  public Game() {
    valid = true;
    next = PLAYER_O;
    board[PLAYER_O] = board[PLAYER_X] = 0x0;
  }

  public GameResult move(int player, int move) {
    GameResult res;
    if (valid && player == next && isFree(move, board)) {
      board[player] += masks[move];
      boolean end = isWon(player, board);
      if (!end) next = (player + 1) & 0x1;
      if ((board[0] + board[1]) == TIED) {
        end = true;
        valid = false;
      }
      res = new GameResult(next, valid, end, board);
    } else {
      valid = false;
      res = new GameResult(player, false, true, board);
    }
    return res;
  }

  static int[] winningBoards = {0x49, 0x92, 0x124, 0x7, 0x38, 0x1c0, 0x111, 0x54};

  private static boolean isWon(int player, int[] board) {
    boolean res = false;
    for (int i = 0; i < winningBoards.length; i++)
      if ((board[player] & winningBoards[i]) == winningBoards[i]) res = true;
    return res;
  }

  static boolean isFree(int move, int[] board) {
    return (board[0] & masks[move]) == 0 && (board[1] & masks[move]) == 0;
  }

  public GameResult status() {
    return new GameResult(next, valid, isWon(next, board), board);
  }
}
