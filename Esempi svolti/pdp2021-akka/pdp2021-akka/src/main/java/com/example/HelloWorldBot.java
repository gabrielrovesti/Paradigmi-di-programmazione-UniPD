package com.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

/**
 * Comportamento di un attore che risponde al saluto
 */
public class HelloWorldBot extends AbstractBehavior<HelloWorld.Greeted> {

  /**
   * Crea il comportamento iniziale
   * 
   * @param max Massimo numero di risposte
   * @return
   */
  public static Behavior<HelloWorld.Greeted> create(int max) {
    return Behaviors.setup(context -> new HelloWorldBot(context, max));
  }

  /**
   * Stato dell'attore
   */
  private final int max;
  private int greetingCounter;

  /**
   * Costruttore di default
   * @param context
   * @param max
   */
  private HelloWorldBot(ActorContext<HelloWorld.Greeted> context, int max) {
    super(context);
    this.max = max;
  }

  /**
   * Fornitore del comportamento
   */
  @Override
  public Receive<HelloWorld.Greeted> createReceive() {
    return newReceiveBuilder().onMessage(HelloWorld.Greeted.class, this::onGreeted).build();
  }

  /**
   * Comportamento alla ricezione del messaggio
   * @param message Messaggio ricevuto
   * @return nuovo comportamento dopo ricezione del messggio
   */
  private Behavior<HelloWorld.Greeted> onGreeted(HelloWorld.Greeted message) {
    greetingCounter++;
    getContext().getLog().info("Greeting {} for {}", greetingCounter, message.whom);
    if (greetingCounter == max) {
      return Behaviors.stopped();
    } else {
      message.from.tell(new HelloWorld.Greet(message.whom, getContext().getSelf()));
      return this;
    }
  }
}
