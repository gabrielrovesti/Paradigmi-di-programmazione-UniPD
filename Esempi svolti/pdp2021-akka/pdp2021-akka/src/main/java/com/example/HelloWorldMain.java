package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

/**
 * Attore che avvia gli altri
 */
public class HelloWorldMain extends AbstractBehavior<HelloWorldMain.SayHello> {

  /**
   * Messaggio: avvia il sistema
   */
  public static class SayHello {
    public final String name;

    public SayHello(String name) {
      this.name = name;
    }
  }

  /**
   * Attore che risponde ai saluti
   */
  private final ActorRef<HelloWorld.Greet> greeter;

  /**
   * Comportamento alla creazione
   */
  public static Behavior<SayHello> create() {
    return Behaviors.setup(HelloWorldMain::new);
  }

  /**
   * Costruttore
   * @param context
   */
  private HelloWorldMain(ActorContext<SayHello> context) {
    super(context);
    greeter = context.spawn(HelloWorld.create(), "greeter");
  }

  /**
   * Fornitore del comportamento
   */
  @Override
  public Receive<SayHello> createReceive() {
    return newReceiveBuilder().onMessage(SayHello.class, this::onSayHello).build();
  }

  /**
   * Comportamento alla ricezione del messaggio
   * @param command
   * @return
   */
  private Behavior<SayHello> onSayHello(SayHello command) {
    ActorRef<HelloWorld.Greeted> replyTo = getContext().spawn(HelloWorldBot.create(3), command.name);
    greeter.tell(new HelloWorld.Greet(command.name, replyTo));
    return this;
  }
}
