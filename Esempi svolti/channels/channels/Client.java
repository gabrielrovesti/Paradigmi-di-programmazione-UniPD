package it.unipd.pdp2021.channels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Random;

public class Client {

  static Random rnd = new Random(new Date().getTime());

  public static void main(String args[]) throws IOException, InterruptedException {
    playGame();
  }

  private static void playGame() throws UnknownHostException, IOException, InterruptedException {
    System.out.println("Client setting up:");
    try (Socket socket = new Socket("127.0.0.1", 56763);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); ) {
      System.out.println("Connected.");
      String line;
      boolean done = false;
      while (!done && (line = in.readLine()) != null) {
        Thread.sleep(1000);
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
}
