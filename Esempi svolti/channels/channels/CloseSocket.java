package it.unipd.pdp2021.channels;

import java.io.IOException;

/**
 * Step: Close a socket
 *
 * <p>There is no next step.
 */
class CloseSocket extends Step<Integer> {

  private int idx;

  CloseSocket(int idx) {
    this.idx = idx;
  }

  @Override
  public void completed(Integer result, GameAttachment attachment) {
    try {
      attachment.players[idx].close();
      // If we are closing player 1, the game is no more in flight
      if (idx == 1)
        attachment.inFlight.decrementAndGet();
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
}
