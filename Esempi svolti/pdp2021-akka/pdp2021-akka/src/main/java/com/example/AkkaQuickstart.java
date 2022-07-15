package com.example;

import akka.actor.typed.ActorSystem;
import java.io.IOException;

/**
 * Punto di ingresso
 */
public class AkkaQuickstart {
  public static void main(String[] args) {
    
    /**
     * Actor System
     */
    final ActorSystem<HelloWorldMain.SayHello> greeterMain =
        ActorSystem.create(HelloWorldMain.create(), "helloakka");

    greeterMain.tell(new HelloWorldMain.SayHello("Charles"));

    try {
      System.out.println(">>> Press ENTER to exit <<<");
      System.in.read();
    } catch (IOException ignored) {
    } finally {
      greeterMain.terminate();
    }
  }
}
