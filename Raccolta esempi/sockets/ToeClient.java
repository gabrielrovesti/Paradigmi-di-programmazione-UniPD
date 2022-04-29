package it.unipd.pdp2021.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Random;

public class ToeClient {

  static Random rnd = new Random(new Date().getTime());

  public static void main(String args[]) throws IOException {
    // Test di connessione
    // connectionTest(args[0]);
    // Gioco completo
    playGame();
  }

  private static void playGame() throws UnknownHostException, IOException {
    System.out.println("Client setting up:");
    try (Socket socket = new Socket("127.0.0.1", ToeServer.PORT_GAME);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
      System.out.println("Connected.");
      String line;
      boolean done = false;
      while (!done && (line = in.readLine()) != null) {
        if (line.startsWith("PLAYER")) {
          // gestiamo una mossa
          System.out.println(line);
          line = in.readLine(); // prima riga
          System.out.println(line);
          line = in.readLine(); // seconda riga
          System.out.println(line);
          line = in.readLine(); // terza riga
          System.out.println(line);
          line = in.readLine(); // mosse disponibili
          System.out.println(line);
          String[] split = line.split("\\s");
          String move = split[rnd.nextInt(split.length)];
          out.println(move);
          System.out.println(move);
          out.flush();
        } else if (line.startsWith("Hello")) {
          // partita iniziata
          System.out.println(line);
        } else {
          // partita finita
          done = true;
          System.out.println(line);
        }
      }
    }
    System.out.println("Client closed.");
  }

  static void connectionTest(String msg) throws IOException, UnknownHostException {
    System.out.println("Client setting up:");
    try (Socket socket = new Socket("127.0.0.1", ToeServer.PORT_TEST);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); ) {
      System.out.println("Connected, sending " + msg);
      out.println(msg);
      System.out.println("Got back: " + in.readLine());
    }
    System.out.println("Client closed.");
  }
}
