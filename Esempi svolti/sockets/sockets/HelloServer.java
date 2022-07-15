package it.unipd.pdp2021.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/** Responds with "Hello!" on port 58324. */
public class HelloServer {
  public static void main(String args[]) throws IOException {

    int portNumber = 58324;

    System.out.println("Server setting up:");
    try (ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket socket = serverSocket.accept();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); ) {
      String inputLine;
      System.out.println("Received data.");
      while ((inputLine = in.readLine()) != null) {
        System.out.println("Received: " + inputLine);
        out.println("Hello " + inputLine);
      }
      System.out.println("Server closing.");
    }
  }
}
