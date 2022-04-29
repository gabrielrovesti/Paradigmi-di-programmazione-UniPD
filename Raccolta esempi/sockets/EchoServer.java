package it.unipd.pdp2021.sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/** Runnable Echo server on UDP */
class EchoServerRunnable implements Runnable {

  private DatagramSocket socket;

  public EchoServerRunnable(int port) throws SocketException {
    socket = new DatagramSocket(port);
  }

  @Override
  public void run() {
    byte[] buf = new byte[256];
    DatagramPacket packet = new DatagramPacket(buf, buf.length);
    System.out.println("Server setup. Receiving...");
    try {
      socket.receive(packet);
      String received = new String(packet.getData(), 0, packet.getLength());
      System.out.println("Received: " + received);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      socket.close();
    }
  }
}

/** Echoes what it receives via datagrams. */
public class EchoServer {
  public static int PORT = 58325;

  public static void main(String args[]) throws IOException {

    new Thread(new EchoServerRunnable(PORT)).run();
  }
}
