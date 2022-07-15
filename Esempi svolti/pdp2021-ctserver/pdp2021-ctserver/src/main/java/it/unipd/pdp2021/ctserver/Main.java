package it.unipd.pdp2021.ctserver;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) {

    // Preparazione dell'infrastruttura
    Vertx vertx = Vertx.vertx();
    HttpServerOptions options = new HttpServerOptions().setLogActivity(true);
    HttpServer server = vertx.createHttpServer(options);
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    // Una struttura dati thread-safe
    final SortedSet<Integer> positives = new ConcurrentSkipListSet<>();

    // Lettura: rispondi con i dati memorizzati, uno per riga
    router
        .get("/")
        .produces("application/text")
        .handler(
            ctx -> {
              ctx.response()
                  .end(
                      positives.stream().map(i -> i.toString()).collect(Collectors.joining("\n"))
                          + "\n");
            });

    // Scrittura: aggiungi i dati ricevuti a quelli noti
    router
        .post("/")
        .handler(
            ctx -> {
              String[] lines = ctx.getBodyAsString().split("\n");
              for (int i = 0; i < lines.length; i++)
                try {
                  positives.add(Integer.parseInt(lines[i]));
                } catch (Exception e) {
                  System.out.println("Invalid: " + lines[i]);
                }
              ctx.response().end();
            });

    // avvia il server
    server.requestHandler(router).listen(8080);
  }
}
