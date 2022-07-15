package it.unipd.pdp2021.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlGet {
  public static void main(String[] args) throws MalformedURLException, IOException {
    BufferedReader reader =
        new BufferedReader(new InputStreamReader(new URL("https://httpbin.org/get").openStream()));
    String line;
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
    }
  }
}
