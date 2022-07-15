package it.unipd.pdp2021.sync;

public class LockedFriends {

  /** Cfr: https://docs.oracle.com/javase/tutorial/essential/concurrency/newlocks.html */
  public static void main(String[] args) {
    final LockedFriend alphonse = new LockedFriend("Alphonse");
    final LockedFriend gaston = new LockedFriend("Gaston");
    new Thread(new BowLoop(alphonse, gaston)).start();
    new Thread(new BowLoop(gaston, alphonse)).start();
  }
}
