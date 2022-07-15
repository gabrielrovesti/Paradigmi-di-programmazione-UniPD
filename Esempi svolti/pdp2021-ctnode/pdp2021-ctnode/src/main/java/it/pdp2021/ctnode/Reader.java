package it.pdp2021.ctnode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class Reader implements Runnable {

  private final Store recorder;
  private final String hqurl;
  public boolean done = false;

  /**
   * @param recorder Deposito dei messaggi finora ricevuti
   * @param hqurl Indirizzo del servizio di segnalazione
   */
  Reader(Store recorder, String hqurl) {
    this.recorder = recorder;
    this.hqurl = hqurl;
  }

  @Override
  public void run() {

    // finché non viene detto di fermarci
    while (!done) {

      // Apri l'url del servizio centrale in lettura
      try (BufferedReader buf =
          new BufferedReader(new InputStreamReader(new URL(hqurl).openStream()))) {

        // leggi la risposta: ogni riga non vuota, un messaggio segnalato
        List<String> res = new ArrayList<>();
        String line = buf.readLine();
        while (line != null && !line.isEmpty()) {
          res.add(line);
          line = buf.readLine();
        }
        buf.close();

        // Se ci sono segnalazioni
        if (!res.isEmpty()) {
          // System.out.println("Read from hq: " + res.stream().collect(Collectors.joining(" ")));

          // Trova le segnalazioni che abbiamo ricevuto
          List<String> matches =
              res.stream().filter(s -> recorder.contains(s)).collect(Collectors.toList());

          // Se ce ne sono, stampale
          if (matches.size() > 0) {
            System.out.println(
                "Matches found! : " + matches.stream().collect(Collectors.joining(" ")));
          }
        }
        TimeUnit.SECONDS.sleep(5);

      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (MalformedURLException e) {
        // Se l'url non è corretta, è inutile riprovare
        done = true;
        e.printStackTrace();
      } catch (IOException e) {

        // Se abbiamo problemi a collegarci, riproviamo dopo un po'
        try {
          TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException t) {
          t.printStackTrace();
        }

        e.printStackTrace();
      }
    }
  }
}
