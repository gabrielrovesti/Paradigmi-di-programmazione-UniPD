package com.example;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import org.junit.ClassRule;
import org.junit.Test;

public class AkkaQuickstartTest {

  @ClassRule public static final TestKitJunitResource testKit = new TestKitJunitResource();

  @Test
  public void testGreeterActorSendingOfGreeting() {
    TestProbe<HelloWorld.Greeted> testProbe = testKit.createTestProbe();
    ActorRef<HelloWorld.Greet> underTest = testKit.spawn(HelloWorld.create(), "greeter");
    underTest.tell(new HelloWorld.Greet("Charles", testProbe.getRef()));
    testProbe.expectMessage(new HelloWorld.Greeted("Charles", underTest));
  }

}
