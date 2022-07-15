package it.unipd.pdp2021.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/** Server di gioco completo. */
class GameServer implements Runnable {

  int port;
  Socket[] sockets = new Socket[2];
  PrintWriter[] outs = new PrintWriter[2];
  BufferedReader[] ins = new BufferedReader[2];
  Game game = new Game();

  GameServer(int port) {
    this.port = port;
  }

  @Override
  public void run() {

    System.out.println("Game setting up:");
    try (ServerSocket serverSocket = new ServerSocket(port); ) {
      // attendi che i giocatori si colleghino
      connectPlayers(serverSocket);
      // dai al primo giocatore la situazione iniziale
      GameResult status = game.status();
      outs[0].println(status);
      outs[0].flush();
      // finché la partita non è conclusa...
      while (!status.end) {
        // attendi la mossa dal giocatore
        String move = ins[status.next].readLine();
        // eseguila
        status = game.move(status.next, Integer.parseInt(move));
        if (!status.end) {
          // informa l'altro giocatore
          outs[status.next].println(status);
          outs[status.next].flush();
        }
      }
      // comunica il risultato
      System.out.println(status);
      if (status.valid) {
        outs[status.next].println("You won.");
        outs[(status.next + 1) & 0x1].println("You lost.");
        System.out.println("Player " + (status.next == 0 ? "O" : "X") + " won.");
      } else {
        outs[0].println("Tied.");
        outs[1].println("Tied.");
        System.out.println("The game is a tie.");
      }

      // chiudi le risorse
      closeResources();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Server closing.");
  }

  void closeResources() throws IOException {
    outs[0].close();
    outs[1].close();
    ins[0].close();
    ins[1].close();
    sockets[0].close();
    sockets[1].close();
  }

  void connectPlayers(ServerSocket serverSocket) throws IOException {
    sockets[0] = serverSocket.accept();
    outs[0] = new PrintWriter(sockets[0].getOutputStream(), true);
    ins[0] = new BufferedReader(new InputStreamReader(sockets[0].getInputStream()));
    outs[0].write("Hello player O\n");
    outs[0].flush();
    System.out.println("Player O connected");
    sockets[1] = serverSocket.accept();
    outs[1] = new PrintWriter(sockets[1].getOutputStream(), true);
    ins[1] = new BufferedReader(new InputStreamReader(sockets[1].getInputStream()));
    outs[1].write("Hello player X\n");
    outs[1].flush();
    System.out.println("Player X connected");
  }
}

/** Prima versione, per testare il collegamento dei due giocatori. */
class TestServer implements Runnable {

  int port;
  Socket[] sockets = new Socket[2];
  PrintWriter[] outs = new PrintWriter[2];
  BufferedReader[] ins = new BufferedReader[2];

  TestServer(int port) {
    this.port = port;
  }

  @Override
  public void run() {
    System.out.println("Game setting up:");
    try (ServerSocket serverSocket = new ServerSocket(port); ) {
      sockets[0] = serverSocket.accept();
      outs[0] = new PrintWriter(sockets[0].getOutputStream(), true);
      ins[0] = new BufferedReader(new InputStreamReader(sockets[0].getInputStream()));
      outs[0].write("Hello player O\n");
      outs[0].flush();
      System.out.println("Player O connected");
      sockets[1] = serverSocket.accept();
      outs[1] = new PrintWriter(sockets[1].getOutputStream(), true);
      ins[1] = new BufferedReader(new InputStreamReader(sockets[1].getInputStream()));
      outs[1].write("Hello player X\n");
      outs[1].flush();
      System.out.println("Player X connected");
      outs[0].close();
      outs[1].close();
      ins[0].close();
      ins[1].close();
      sockets[0].close();
      sockets[1].close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Server closing.");
  }
}

public class ToeServer {

  public static int PORT_TEST = 59321;
  public static int PORT_GAME = 59322;

  public static void main(String args[]) throws IOException {
    // prima versione per testare il collegamento
    // new Thread(new GameServer1(PORT_TEST)).start();
    // seconda versione con il gioco completo
    new Thread(new GameServer(PORT_GAME)).start();
  }
}
