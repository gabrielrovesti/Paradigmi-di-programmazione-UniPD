package it.unipd.pdp2021.channels;

import it.unipd.pdp2021.sockets.GameResult;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Step: Check the game status and write to next player
 *
 * <p>Next: Read player move if the game continues, send win/lose messages and close sockets if the
 * game has a winner, send tie messages and close sockets if the game is a tie
 */
class WriteStatus extends Step<Integer> {
  @Override
  public void completed(Integer result, GameAttachment attachment) {

    String input = new String(attachment.readBuf.array(), 0, result).trim();
    // read received data
    Integer move = Integer.parseInt(input);
    GameResult initial = attachment.game.status();

    System.out.println(
        Thread.currentThread().getName()
            + " : game "
            + attachment.id
            + " read "
            + move
            + " from player "
            + (initial.next == 0 ? "O" : "X"));

    GameResult status = attachment.game.move(initial.next, move);

    if (!status.end) {
      // the game goes on
      AsynchronousSocketChannel socket = attachment.players[status.next];
      byte[] bytes = (status.toString() + "\n").getBytes();
      socket.write(wrap(bytes), attachment, new ReadPlayer());
    } else if (status.valid) {
      System.out.println(
          Thread.currentThread().getName()
              + " : game "
              + attachment.id
              + " ended with winner "
              + (status.next == 0 ? "O" : "X"));
      // we have a winner
      attachment.players[status.next].write(
          wrap("You won.".getBytes()), attachment, new CloseSocket(status.next));
      int loser = (status.next + 1) & 0x1;
      attachment.players[loser].write(
          wrap("You lost.".getBytes()), attachment, new CloseSocket(loser));
    } else {
      // we have a tie
      System.out.println(Thread.currentThread().getName() + " : game " + attachment.id + " tied");
      attachment.players[0].write(wrap("Tied.".getBytes()), attachment, new CloseSocket(0));
      attachment.players[1].write(wrap("Tied.".getBytes()), attachment, new CloseSocket(1));
    }
  }
}
