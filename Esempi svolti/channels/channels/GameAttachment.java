package it.unipd.pdp2021.channels;

import it.unipd.pdp2021.sockets.Game;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/** Management status of a single game. */
class GameAttachment {
  final int id;
  final Game game;
  final AsynchronousServerSocketChannel server;
  final AsynchronousChannelGroup group;
  final AsynchronousSocketChannel players[];
  final ByteBuffer readBuf = ByteBuffer.allocate(4);
  final AtomicInteger inFlight;

  /**
   * Constructor for the start of the game (increments inFlight)
   * 
   * @param id     Game id
   * @param game   Game status
   * @param server Communication Channel
   */
  GameAttachment(
      int id, Game game, AsynchronousServerSocketChannel server, AsynchronousChannelGroup group, AtomicInteger inFlight) {
    this.id = id;
    this.game = game;
    this.server = server;
    this.group = group;
    this.players = new AsynchronousSocketChannel[] {};
    this.inFlight = inFlight;
    inFlight.incrementAndGet();
  }

  /**
   * Constructor after the joining of the first player
   * @param id
   * @param game
   * @param server
   * @param playerO
   * @param group
   * @param inFlight
   */
  private GameAttachment(
      int id,
      Game game,
      AsynchronousServerSocketChannel server,
      AsynchronousSocketChannel playerO,
      AsynchronousChannelGroup group, AtomicInteger inFlight) {
    this.id = id;
    this.game = game;
    this.server = server;
    this.group = group;
    this.players = new AsynchronousSocketChannel[] { playerO };
    this.inFlight = inFlight;
  }

  /**
   * Constructor after both player joined
   * 
   * @param id
   * @param game
   * @param server
   * @param playerO
   * @param playerX
   * @param group
   * @param inFlight
   */
  private GameAttachment(
      int id,
      Game game,
      AsynchronousServerSocketChannel server,
      AsynchronousSocketChannel playerO,
      AsynchronousSocketChannel playerX,
      AsynchronousChannelGroup group, AtomicInteger inFlight) {
    this.id = id;
    this.game = game;
    this.server = server;
    this.group = group;
    this.players = new AsynchronousSocketChannel[] { playerO, playerX };
    this.inFlight = inFlight;
  }

  /**
   * Build the game status for player O
   *
   * @param socket communication channel
   * @return The staus set up for player O
   */
  GameAttachment playerO(AsynchronousSocketChannel socket) {
    return new GameAttachment(id, game, server, socket, group, inFlight);
  }

  /**
   * Build the game status for player O
   *
   * @param socket communication channel
   * @return The staus set up for player O
   */
  GameAttachment playerX(AsynchronousSocketChannel socket) {
    return new GameAttachment(id, game, server, players[0], socket, group, inFlight);
  }

  /**
   * This game as ended. Decrement in flight
   */
  int done() {
    return inFlight.decrementAndGet();
  }
}
