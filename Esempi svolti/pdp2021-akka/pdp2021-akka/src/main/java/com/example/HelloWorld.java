package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import java.util.Objects;

/**
 * Comportamento per l'attore che risponde al saluto.
 */
public class HelloWorld extends AbstractBehavior<HelloWorld.Greet> {

  /**
   * Messaggio di saluto
   */
  public static final class Greet {
    public final String whom;
    public final ActorRef<Greeted> replyTo;

    /**
     * 
     * @param whom Nome del destinatario
     * @param replyTo Mittente
     */
    public Greet(String whom, ActorRef<Greeted> replyTo) {
      this.whom = whom;
      this.replyTo = replyTo;
    }
  }

  /**
   * Messaggio di risposta al saluto
   */
  public static final class Greeted {
    public final String whom;
    public final ActorRef<Greet> from;

    public Greeted(String whom, ActorRef<Greet> from) {
      this.whom = whom;
      this.from = from;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Greeted greeted = (Greeted) o;
      return Objects.equals(whom, greeted.whom) && Objects.equals(from, greeted.from);
    }

    @Override
    public int hashCode() {
      return Objects.hash(whom, from);
    }

    @Override
    public String toString() {
      return "Greeted{" + "whom='" + whom + '\'' + ", from=" + from + '}';
    }
  }

  /**
   * Comportamento alla creazione.
   */
  public static Behavior<Greet> create() {
    return Behaviors.setup(HelloWorld::new);
  }

  /**
   * Costruttore del comportamento
   * 
   * @param context
   */
  private HelloWorld(ActorContext<Greet> context) {
    super(context);
  }

  /**
   * Fabbrica del comportamento dell'attore
   */
  @Override
  public Receive<Greet> createReceive() {
    return newReceiveBuilder().onMessage(Greet.class, this::onGreet).build();
  }

  /**
   * Definizione del comportamento dell'attore
   * 
   * @param command
   * @return
   */
  private Behavior<Greet> onGreet(Greet command) {
    getContext().getLog().info("Hello {}!", command.whom);
    command.replyTo.tell(new Greeted(command.whom, getContext().getSelf()));
    return this;
  }
}
