package it.unipd.pdp2021.sync;

/** A simplistic actor that syncronizes the greeting with another instance */
class SimpleFriend {
  private final String name;

  public SimpleFriend(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public synchronized void bow(SimpleFriend bower) {
    System.out.format("%s: %s" + "  has bowed to me!%n", this.name, bower.getName());
    bower.bowBack(this);
  }

  public synchronized void bowBack(SimpleFriend bower) {
    System.out.format("%s: %s" + " has bowed back to me!%n", this.name, bower.getName());
  }
}

/** Cfr: https://docs.oracle.com/javase/tutorial/essential/concurrency/deadlock.html */
public class SimpleFriends {

  public static void main(String[] args) {
    final SimpleFriend alphonse = new SimpleFriend("Alphonse");
    final SimpleFriend gaston = new SimpleFriend("Gaston");
    new Thread(() -> alphonse.bow(gaston)).start();
    new Thread(() -> gaston.bow(alphonse)).start();
  }
}
