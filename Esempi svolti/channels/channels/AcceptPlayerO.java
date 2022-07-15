package it.unipd.pdp2021.channels;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * First step: accept player O
 *
 * <p>Next: accept player X
 */
class AcceptPlayerO extends Step<AsynchronousSocketChannel> {

  @Override
  public void completed(AsynchronousSocketChannel result, GameAttachment attachment) {
    System.out.println(
        Thread.currentThread().getName() + " : game " + attachment.id + " connected player O");
    attachment.server.accept(attachment.playerO(result), new WriteFirstStatus());
  }
}
