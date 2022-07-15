package it.pdp2021.ctnode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;

/** Ricevitore. Ascolta i messaggi e li accoda. */
class Receiver implements Runnable {

  private final int port;
  private final InetAddress local;
  private BlockingQueue<String> queue;

  public boolean done = false;

  /**
   * @param local indirizzo locale, per riconoscere la provenienza dei messaggi del proprio beacon
   * @param queue coda su cui indirizzare i messaggi ricevuti
   * @param port porta su cui ascoltare
   */
  Receiver(InetAddress local, BlockingQueue<String> queue, int port) {
    this.local = local;
    this.queue = queue;
    this.port = port;
  }

  @Override
  public void run() {
    byte[] buf = new byte[256];
    DatagramPacket packet = new DatagramPacket(buf, buf.length);
    try (DatagramSocket socket = new DatagramSocket(port); ) {

      // receive() attende al massimo 2000ms
      socket.setSoTimeout(2000);

      // finchè non ci viene chiesto di terminare...
      while (!done) {

        // attendi un paccketto
        try {
          socket.receive(packet);

          // ottieni il messaggio
          String received = new String(packet.getData(), 0, packet.getLength());

          // controlla la provenienza, per non registrare erronemente messaggi dello stesso beacon
          InetAddress addr = packet.getAddress();
          if (!addr.equals(local)) {

            // accoda il messaggio ricevuto
            queue.offer(received);
            System.out.println("Received: " + received + " from " + addr.toString());
          }
        } catch (SocketTimeoutException e) {
          // nessun problema, riproviamo se necessario
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
