package it.unipd.pdp2021.sync;

import java.util.Random;

public class LockedBuffer {
  public static void main(String[] args) {
    CharSource source = new CharSource(100, 100);
    Buffer buffer = new Buffer(20);

    Thread producer = new Thread(new Producer(source, buffer), "producer");

    Thread[] consumers =
        new Thread[] {
          new Thread(new Consumer(buffer)),
          new Thread(new Consumer(buffer)),
          new Thread(new Consumer(buffer))
        };

    producer.start();
    for (Thread t : consumers) t.start();
  }

  static void randomWait(int max) {
    try {
      Random random = new Random();
      Thread.sleep(random.nextInt(max));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
