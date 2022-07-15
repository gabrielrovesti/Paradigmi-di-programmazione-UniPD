package it.unipd.pdp2021.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import it.unipd.pdp2021.sockets.GameResult;

public class Server {

  private static final String GAME_NOT_FOUND = "<html><body><h1>Game not found.</h1></body></html>";

  private static final String JOIN_FORM =
      "<html><body><form action='/game' method='POST'>"
          + "<input type='submit' value='Join a game'/></form></body></html>";

  private static final String WAIT_FOR_ANOTHER =
      "<html><body><h3>Please wait for another player.</h3><a href='/game/%s"
          + "'>Click here to refresh</a></body></html>";

  static final ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args) {

    // The Game Server
    GameServer gameServer = new GameServer();

    // The infrastructure parts
    Vertx vertx = Vertx.vertx();
    HttpServerOptions options = new HttpServerOptions().setLogActivity(true);
    HttpServer server = vertx.createHttpServer(options);
    Router router = Router.router(vertx);
    // setup body handling for all routes
    router.route().handler(BodyHandler.create());

    // Browser entry point
    router
        .get("/")
        .produces("text/html")
        .handler(
            ctx -> {
              ctx.response().end(JOIN_FORM);
            });

    // POST /game - want to play
    router
        .post("/game")
        .produces("text/html")
        .handler(
            ctx -> {
              GameLocation location = new GameLocation(gameServer.create());
              ctx.response().setStatusCode(302).putHeader("Location", location.game).end();
            });

    router
        .post("/game")
        .produces("application/json")
        .handler(
            ctx -> {
              GameLocation location = new GameLocation(gameServer.create());
              ctx.response()
                  .putHeader("content-type", "application/json")
                  .end(location.toJson(mapper));
            });

    // POST /game/{id} move=x - play move x
    router
        .post("/game/:playerId")
        .produces("text/html")
        .handler(
            ctx -> {
              String playerId = ctx.request().getParam("playerId");
              boolean open = gameServer.open(playerId);
              if (!open) ctx.response().setStatusCode(404).end(GAME_NOT_FOUND);

              String result =
                  gameServer
                      .status(playerId)
                      .map(
                          (res) -> {
                            int idx = res.idx();
                            GameResult status = res.status();
                            int move = Integer.valueOf(ctx.request().getParam("move"));
                            if (idx == status.next && status.valid && !status.end)
                              status = gameServer.player(playerId, move).orElse(status);
                            return render(playerId, idx, status);
                          })
                      .orElse(String.format(WAIT_FOR_ANOTHER, playerId));

              ctx.response().end(result);
            });

    router
        .post("/game/:playerId")
        .produces("application/json")
        .handler(
            ctx -> {
              String playerId = ctx.request().getParam("playerId");
              boolean open = gameServer.open(playerId);
              if (!open) ctx.response().setStatusCode(404).end(GAME_NOT_FOUND);

              String result =
                  gameServer
                      .status(playerId)
                      .map(
                          (res) -> {
                            int idx = res.idx();
                            GameResult status = res.status();
                            int move = Integer.valueOf(ctx.request().getParam("move"));
                            if (idx == status.next && status.valid && !status.end)
                              status = gameServer.player(playerId, move).orElse(status);
                            return renderJson(playerId, idx, status);
                          })
                      .orElse(new GameStatus("wait for another").toJson(mapper));

              ctx.response().end(result);
            });

    // GET /game/{id} - read status
    router
        .get("/game/:playerId")
        .produces("text/html")
        .handler(
            ctx -> {
              String playerId = ctx.request().getParam("playerId");
              boolean open = gameServer.open(playerId);
              if (!open) ctx.response().setStatusCode(404).end(GAME_NOT_FOUND);

              String result =
                  gameServer
                      .status(playerId)
                      .map((res) -> render(playerId, res.idx(), res.status()))
                      .orElse(String.format(WAIT_FOR_ANOTHER, playerId));
              ctx.response().end(result);
            });

    router
        .get("/game/:playerId")
        .produces("application/json")
        .handler(
            ctx -> {
              String playerId = ctx.request().getParam("playerId");
              boolean open = gameServer.open(playerId);

              if (!open) ctx.response().setStatusCode(404).end(GAME_NOT_FOUND);

              String result =
                  gameServer
                      .status(playerId)
                      .map((res) -> renderJson(playerId, res.idx(), res.status()))
                      .orElse(new GameStatus("wait for another").toJson(mapper));
              ctx.response().end(result);
            });

    server.requestHandler(router).listen(8080);
  }

  static String render(String gameId, int idx, GameResult game) {
    StringBuffer res = new StringBuffer("<html><body>");

    String playerName = idx == 0 ? "Player O" : "Player X";

    res = res.append("<h2>").append(playerName).append("</h2><table border='1'>\n");
    String[] split = game.board.split("\\n");

    if (split.length == 4) {
      res =
          res.append("<tr><td>").append(split[0].replace(" ", "</td><td>")).append("</td></tr>\n");
      res =
          res.append("<tr><td>").append(split[1].replace(" ", "</td><td>")).append("</td></tr>\n");
      res =
          res.append("<tr><td>").append(split[2].replace(" ", "</td><td>")).append("</td></tr>\n");
      res = res.append("\n</table>");
    }

    if (game.next == idx && !game.end && game.valid) {
      res.append("<form method='POST'>");
      for (String move : split[3].split("\\s")) {
        res.append("<input type='radio' name='move' value='")
            .append(move)
            .append("' />")
            .append(move)
            .append("\n");
      }
      res.append("<br/><input type='submit' value='Move' /></form>");
    } else if (game.next == idx && game.end && game.valid) {
      res.append("You won!");
    } else if (game.next != idx && game.end && game.valid) {
      res.append("You lost");
    } else if (game.end && !game.valid) {
      res.append("The game is tied");
    } else {
      res.append(
          "Please wait for the other player to move. "
              + "<a href='/game/"
              + gameId
              + "'>Click here to refresh</a>");
    }

    return res.append("</body></html>").toString();
  }

  static String renderJson(String gameId, int idx, GameResult game) {
    StringBuffer res = new StringBuffer("{ \"player\":\"");

    String playerName = idx == 0 ? "O" : "X";

    res = res.append(playerName).append("\",\n\"board\":[");
    String[] split = game.board.split("\\n");

    if (split.length == 4) {
      res =
          res.append("\"")
              .append(split[0].replace(" ", "\",\""))
              .append("\",\"")
              .append(split[1].replace(" ", "\",\""))
              .append("\",\"")
              .append(split[2].replace(" ", "\",\""))
              .append("\"]");
    }

    if (game.next == idx && !game.end && game.valid) {
      res.append(",\n\"gameStatus\":\"waiting your move\",\n");
      res.append("\"availableMoves\":[");
      StringBuffer row = new StringBuffer();
      for (String s : split[3].split("\\s")) {
        row.append("\"" + s + "\",");
      }
      res.append(row.substring(0, row.length() - 1));
      res.append("]");
    } else if (game.next != idx && !game.end && game.valid) {
      res.append(",\n\"gameStatus\":\"waiting other move\"");
    } else if (game.next == idx && game.end && game.valid) {
      res.append(",\n\"gameStatus\":\"ended\",");
      res.append("\"gameResult\":\"won\"");
    } else if (game.next != idx && game.end && game.valid) {
      res.append(",\n\"gameStatus\":\"ended\",");
      res.append("\"gameResult\":\"lost\"");
    } else if (game.end && !game.valid) {
      res.append(",\n\"gameStatus\":\"ended\",");
      res.append("\"gameResult\":\"tied\"");
    }

    return res.append("}\n").toString();
  }
}
