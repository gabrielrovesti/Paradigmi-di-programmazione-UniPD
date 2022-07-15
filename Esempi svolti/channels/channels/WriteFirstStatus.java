package it.unipd.pdp2021.channels;

import it.unipd.pdp2021.sockets.Game;
import it.unipd.pdp2021.sockets.GameResult;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * Step: Write the first game status (no need to check for completed game). Set
 * up the acceptance of
 * new games, if the limit has not been reached yet.
 *
 * <p>
 * Next: Read player's move
 */
class WriteFirstStatus extends Step<AsynchronousSocketChannel> {
  @Override
  public void completed(AsynchronousSocketChannel result, GameAttachment attachment) {

    System.out.println(
        Thread.currentThread().getName() + " : game " + attachment.id + " connected for player X");
    attachment = attachment.playerX(result);

    GameResult status = attachment.game.status();
    AsynchronousSocketChannel socket = attachment.players[status.next];
    byte[] bytes = (status.toString() + "\n").getBytes();
    System.out.println(Thread.currentThread().getName() + " : game " + attachment.id + " started");
    socket.write(wrap(bytes), attachment, new ReadPlayer());

    // more games?
    if (attachment.id <= 5) {
      attachment.server.accept(
          new GameAttachment(attachment.id + 1, new Game(), attachment.server, attachment.group, attachment.inFlight),
          new AcceptPlayerO());
    } else {
      System.out.println("Shutting down...");
      attachment.group.shutdown();
    }
  }
}
