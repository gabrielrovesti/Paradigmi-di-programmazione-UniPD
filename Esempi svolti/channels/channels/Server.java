package it.unipd.pdp2021.channels;

import it.unipd.pdp2021.sockets.Game;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

  public static final int GAME_PORT = 56763;

  public static void main(String[] args) throws IOException, InterruptedException {

    AtomicInteger inFlight = new AtomicInteger(0);
    ExecutorService pool = Executors.newFixedThreadPool(4);
    AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(pool);
    AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open(group)
        .bind(new InetSocketAddress("127.0.0.1", Server.GAME_PORT), 16);

    System.out.println("Accepting...");

    serverSocket.accept(
        new GameAttachment(1, new Game(), serverSocket, group, inFlight), new AcceptPlayerO());

    while (!group.awaitTermination(5, TimeUnit.SECONDS) && inFlight.get() > 0) ;
    // System.out.println("Closing socket");
    serverSocket.close();
  }
}
