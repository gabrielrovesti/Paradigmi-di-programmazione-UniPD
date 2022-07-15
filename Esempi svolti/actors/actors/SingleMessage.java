package it.unipd.pdp2021.actors;

import static java.lang.System.out;

import java.util.concurrent.Executors;

public class SingleMessage {

  public static void main(String args[]) {

    var actorSystem = new System(Executors.newCachedThreadPool());
    // create an actor
    Address<String> actor =
        actorSystem.actorOf(
            self ->
                msg -> {
                  out.println(
                      "self: " + self + "; got msg: '" + msg + "'; length: " + msg.length());
                  return Effect.die();
                });

    actor.tell("foo").tell("bar");
  }
}
