package it.unipd.pdp2021.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/** Calls the HelloServer. */
public class HelloClient {
  public static void main(String args[]) throws IOException {

    int portNumber = 58324;

    System.out.println("Client setting up:");
    try (Socket socket = new Socket("127.0.0.1", portNumber);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); ) {
      System.out.println("Connected, sending " + args[0]);
      out.println(args[0]);
      System.out.println("Got back: " + in.readLine());
    }
    System.out.println("Client closed.");
  }
}
