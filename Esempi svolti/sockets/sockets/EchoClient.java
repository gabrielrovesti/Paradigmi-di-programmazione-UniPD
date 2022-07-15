package it.unipd.pdp2021.sockets;

import static it.unipd.pdp2021.sockets.EchoServer.PORT;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/** Sends data to the Echo server via UDP */
public class EchoClient {
  public static void main(String args[]) throws IOException {

    DatagramSocket socket = new DatagramSocket();
    byte[] buf = args[0].getBytes();
    InetAddress address = InetAddress.getByName("localhost");
    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
    socket.send(packet);
    socket.close();
  }
}
