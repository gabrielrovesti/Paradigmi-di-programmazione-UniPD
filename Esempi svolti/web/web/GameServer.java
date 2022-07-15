package it.unipd.pdp2021.web;

import it.unipd.pdp2021.sockets.GameResult;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/** A game server, as a thread-safe class. */
class GameServer {

  // active and completed games
  ConcurrentMap<String, NetGame> games = new ConcurrentHashMap<>();

  // awaiting players
  BlockingQueue<NetGame> openings = new LinkedBlockingQueue<>(16);

  private Random random = new Random();

  /**
   * Setup a game
   *
   * @return the new game ID for the player
   */
  String create() {
    String res;
    if (openings.isEmpty()) {
      // open a new game
      res = createPlayerId();
      NetGame netGame = NetGame.open(res);
      games.putIfAbsent(res, netGame);
      openings.offer(netGame);
    } else {
      res = createPlayerId();
      NetGame netGame = openings.poll().match(res);
      games.put(netGame.playerId[0], netGame);
      games.put(netGame.playerId[1], netGame);
    }
    return res;
  }

  private String createPlayerId() {
    String res;
    int newId = random.nextInt(Integer.MAX_VALUE);
    while (games.containsKey("" + newId)) newId = random.nextInt();
    res = "" + newId;
    return res;
  }

  /**
   * Returns the status of the game after the move
   *
   * @param game game where to play; implicitly selects player
   * @param move move to play
   * @return current status of the game
   */
  Optional<GameResult> player(String playerId, int move) {
    Optional<GameResult> res = Optional.empty();
    if (games.containsKey(playerId) && games.get(playerId).started()) {
      int idx = games.get(playerId).playerId[0].equals(playerId) ? 0 : 1;
      res = Optional.of(games.get(playerId).move(idx, move));
    }
    return res;
  }

  /**
   * True if the player is known
   *
   * @param playerId
   * @return
   */
  public boolean open(String playerId) {
    return games.containsKey(playerId);
  }

  /**
   * Return the status of a game for the specified player
   *
   * @param playerId
   * @return
   */
  public Optional<GameIndex> status(String playerId) {
    Optional<GameIndex> res = Optional.empty();
    if (games.containsKey(playerId) && games.get(playerId).started()) {
      int idx = games.get(playerId).playerId[0].equals(playerId) ? 0 : 1;
      res = Optional.of(new GameIndex(idx, games.get(playerId).status()));
    }
    return res;
  }
}
