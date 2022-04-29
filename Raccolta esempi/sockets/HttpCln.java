package it.unipd.pdp2021.sockets;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

public class HttpCln {
  public static void main(String[] args) throws IOException, InterruptedException {

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder().uri(URI.create("https://httpbin.org/get")).build();

    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

    System.out.println(response.statusCode());
    System.out.println(response.body());

    System.out.println("---");

    client
        .sendAsync(request, BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(System.out::println);

    System.out.println("---");

    HttpRequest delete =
        HttpRequest.newBuilder().DELETE().uri(URI.create("https://httpbin.org/delete")).build();

    client
        .sendAsync(delete, BodyHandlers.ofString())
        .thenApply(r -> r.statusCode() + "\n" + r.body())
        .thenAccept(System.out::println)
        .join();

    System.out.println("---");

    HttpRequest post =
        HttpRequest.newBuilder()
            .uri(URI.create("https://httpbin.org/post"))
            .timeout(Duration.ofMinutes(2))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(BodyPublishers.ofString("foo=bar&baz=1"))
            .build();

    client
        .sendAsync(post, BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(System.out::println)
        .join();
  }
}
