package it.unipd.pdp2021.mnaut;

import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;

@Controller("/")
public class Server {

  private static final String GAME_NOT_FOUND = "<html><body><h1>Game not found.</h1></body></html>";

  private static final String JOIN_FORM =
      "<html><body><form action='/game' method='POST'>"
          + "<input type='submit' value='Join a game'/></form></body></html>";

  private static final String WAIT_FOR_ANOTHER =
      "<html><body><h3>Please wait for another player.</h3><a href='/game/%s"
          + "'>Click here to refresh</a></body></html>";

  static final ObjectMapper mapper = new ObjectMapper();

  // The Game Server
  GameServer gameServer = new GameServer();

  @Get(value = "/", produces = MediaType.TEXT_HTML)
  public String welcome() {
    return JOIN_FORM;
  }

  @Post(value="/game", consumes = MediaType.APPLICATION_FORM_URLENCODED, produces=MediaType.TEXT_HTML)
  public HttpResponse<String> join() {
      GameLocation location = new GameLocation(gameServer.create());
      return HttpResponse.redirect(URI.create(location.game));
  }

  @Post(value="/game", consumes = MediaType.APPLICATION_JSON, produces=MediaType.APPLICATION_JSON)
  public HttpResponse<String> joinMachine() {
      GameLocation location = new GameLocation(gameServer.create());
      return HttpResponse.ok(location.toJson(mapper));
  }

  @Get(value = "/game/{playerId}", produces = MediaType.TEXT_HTML)
  public HttpResponse<String> gameStatus(@PathVariable String playerId) {
    boolean open = gameServer.open(playerId);

    if (!open) return HttpResponse.notFound().body(GAME_NOT_FOUND);

    String result =
        gameServer
            .status(playerId)
            .map((res) -> render(playerId, res.idx, res.status))
            .orElse(String.format(WAIT_FOR_ANOTHER, playerId));
    return HttpResponse.ok().body(result);
  }

  @Get(value = "/game/{playerId}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<String> gameStatusApi(@PathVariable String playerId) {
    boolean open = gameServer.open(playerId);

    if (!open) return HttpResponse.notFound().body(GAME_NOT_FOUND);

    String result =
        gameServer
            .status(playerId)
            .map((res) -> renderJson(playerId, res.idx, res.status))
            .orElse(new GameStatus("wait for another").toJson(mapper));
    return HttpResponse.ok().body(result);
  }

  @Post(
      value = "/game{/playerId}",
      produces = MediaType.TEXT_HTML,
      consumes = MediaType.APPLICATION_FORM_URLENCODED)
  public HttpResponse<String> game(@PathVariable String playerId, Integer move) {

    if (playerId == null) {
      GameLocation location = new GameLocation(gameServer.create());
      return HttpResponse.status(HttpStatus.TEMPORARY_REDIRECT)
          .header("Location", location.game)
          .body("");
    }

    boolean open = gameServer.open(playerId);
    if (!open) return HttpResponse.notFound().body(GAME_NOT_FOUND);

    String result =
        gameServer
            .status(playerId)
            .map(
                (res) -> {
                  int idx = res.idx;
                  GameResult status = res.status;
                  if (idx == status.next && status.valid && !status.end)
                    status = gameServer.player(playerId, move).orElse(status);
                  return render(playerId, idx, status);
                })
            .orElse(String.format(WAIT_FOR_ANOTHER, playerId));

    return HttpResponse.ok().body(result);
  }

  @Post(value = "/game{/playerId}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<String> gameApi(@PathVariable String playerId, Integer move) {

    if (playerId == null) {
      GameLocation location = new GameLocation(gameServer.create());
      return HttpResponse.ok().body(location.toJson(mapper));
    }

    boolean open = gameServer.open(playerId);
    if (!open) return HttpResponse.notFound().body(GAME_NOT_FOUND);
    String result =
        gameServer
            .status(playerId)
            .map(
                (res) -> {
                  int idx = res.idx;
                  GameResult status = res.status;
                  if (idx == status.next && status.valid && !status.end)
                    status = gameServer.player(playerId, move).orElse(status);
                  return renderJson(playerId, idx, status);
                })
            .orElse(new GameStatus("wait for another").toJson(mapper));

    return HttpResponse.ok().body(result);
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
