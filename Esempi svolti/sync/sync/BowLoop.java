package it.unipd.pdp2021.sync;

import java.util.Random;

/** Makes two friends bow to each other at random intervals. */
class BowLoop implements Runnable {
  private LockedFriend bower;
  private LockedFriend bowee;

  public BowLoop(LockedFriend bower, LockedFriend bowee) {
    this.bower = bower;
    this.bowee = bowee;
  }

  public void run() {
    Random random = new Random();
    for (int i = 1; i < 20; i++) {
      try {
        Thread.sleep(random.nextInt(10));
      } catch (InterruptedException e) {
      }
      bowee.bow(bower);
    }
  }
}
