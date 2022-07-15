package it.pdp2021.ctnode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

class Beacon implements Runnable {

  private final int port, start;
  private final String address;
  public boolean done = false;

  /**
   * @param address Indirizzo a cui mandare i pacchetti, eventualmente multicast
   * @param port Porta a cui mandare i pacchetti
   * @param start valore iniziale della sequenza
   */
  Beacon(String address, int port, int start) {
    this.address = address;
    this.port = port;
    this.start = start;
  }

  @Override
  public void run() {
    int i = start;

    // non iniziare immediatamente
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }

    try (DatagramSocket socket = new DatagramSocket(); ) {

      // configura l'indirizzo di destinazione
      InetAddress addr = InetAddress.getByName(address);

      // finché non ci dicono di fermarci...
      while (!done) {

        // prepara il messaggio
        byte[] buf = ("" + i).getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, addr, port);

        // invialo, incrementa la sequenza, attendi
        socket.send(packet);
        i++;
        TimeUnit.SECONDS.sleep(3);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
