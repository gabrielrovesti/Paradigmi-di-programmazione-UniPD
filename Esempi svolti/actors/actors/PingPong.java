package it.unipd.pdp2021.actors;

import static java.lang.System.out;

import java.util.concurrent.Executors;

record Ping(Address<Pong> sender) {
};

sealed interface Pong {
};

record SimplePong(Address<Ping> sender) implements Pong {
};

record DeadlyPong(Address<Ping> sender) implements Pong {
};

public class PingPong {

  public static void main(String... args) {
    var actorSystem = new System(Executors.newCachedThreadPool());
    Address<Ping> ponger = actorSystem.actorOf(self -> msg -> pongerBehavior(self, msg, 0));
    Address<Pong> pinger = actorSystem.actorOf(self -> msg -> pingerBehavior(self, msg));
    ponger.tell(new Ping(pinger));
  }

  static Effect<Ping> pongerBehavior(Address<Ping> self, Ping msg, int counter) {
    return switch (msg) {
      case Ping p && counter < 10 -> {
        out.println("ping! ->");
        p.sender().tell(new SimplePong(self));
        yield Effect.become(m -> pongerBehavior(self, m, counter + 1));
      }
      case Ping p -> {
        out.println("ping! x");
        p.sender().tell(new DeadlyPong(self));
        yield Effect.die();
      }
    };
  }

  static Effect<Pong> pingerBehavior(Address<Pong> self, Pong msg) {
    return switch (msg) {
      case SimplePong p -> {
        out.println("pong! <-");
        p.sender().tell(new Ping(self));
        yield Effect.stay();
      }
      case DeadlyPong p -> {
        out.println("pong! X");
        p.sender().tell(new Ping(self));
        yield Effect.die();
      }
    };
  }

}
