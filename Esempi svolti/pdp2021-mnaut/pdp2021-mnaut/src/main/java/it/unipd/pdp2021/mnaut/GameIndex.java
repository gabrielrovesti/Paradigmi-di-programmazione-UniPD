package it.unipd.pdp2021.mnaut;

class GameIndex {
  public final int idx;
  public final GameResult status;

  GameIndex(int idx, GameResult status) {
    this.idx = idx;
    this.status = status;
  }

  public int getIdx() {
    return idx;
  }

  public GameResult getStatus() {
    return status;
  }
}
