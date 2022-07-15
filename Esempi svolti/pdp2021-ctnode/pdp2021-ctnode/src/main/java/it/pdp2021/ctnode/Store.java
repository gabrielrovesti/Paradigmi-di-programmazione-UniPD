package it.pdp2021.ctnode;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

public class Store implements Runnable {

  // Struttura dati con prestazioni logaritmiche, simmetriche in scrittura e lettura.
  private final Set<String> store = new ConcurrentSkipListSet<>();
  private final BlockingQueue<String> received;
  public boolean done = false;

  /**
   * @param received coda da cui prelevare i messaggi
   */
  public Store(BlockingQueue<String> received) {
    this.received = received;
  }

  @Override
  public void run() {

    // finché qualcuno non ci dice di fermarci...
    while (!done) {

      try {

        // prendiamo un messaggio
        String s = received.poll(3, TimeUnit.SECONDS);

        // se effettivamente ne abbiamo ottenuto uno, lo salviamo
        if (s != null) {
          store.add(s);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Questa chiamata è Thread-safe perché la struttura dati sottostante lo è.
   *
   * @param s Messaggio da cercare
   * @return Vero se il messaggio è stato ricevuto in passato
   */
  public boolean contains(String s) {
    return store.contains(s);
  }
}
