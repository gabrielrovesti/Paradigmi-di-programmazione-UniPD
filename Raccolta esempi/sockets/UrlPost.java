package it.unipd.pdp2021.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UrlPost {
  public static final String DEST = "https://httpbin.org/post";

  public static void main(String[] args) throws MalformedURLException, IOException {
    URL url = new URL(DEST);
    URLConnection connection = url.openConnection();
    connection.setDoOutput(true);

    PrintWriter writer = new PrintWriter(connection.getOutputStream());
    writer.println("test=val");
    writer.close();

    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String line;
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
    }
  }
}
