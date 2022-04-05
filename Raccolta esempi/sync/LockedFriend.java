package it.unipd.pdp2021.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** A friend that correctly bows without banging heads. */
class LockedFriend {
  private final String name;
  private final Lock lock = new ReentrantLock();

  public LockedFriend(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public boolean impendingBow(LockedFriend bower) {
    Boolean myLock = false;
    Boolean yourLock = false;
    try {
      myLock = lock.tryLock();
      yourLock = bower.lock.tryLock();
    } finally {
      if (!(myLock && yourLock)) {
        if (myLock) {
          lock.unlock();
        }
        if (yourLock) {
          bower.lock.unlock();
        }
      }
    }
    return myLock && yourLock;
  }

  public void bow(LockedFriend bower) {
    if (impendingBow(bower)) {
      try {
        System.out.format("%s: %s has" + " bowed to me!%n", this.name, bower.getName());
        bower.bowBack(this);
      } finally {
        lock.unlock();
        bower.lock.unlock();
      }
    } else {
      System.out.format(
          "%s: %s started" + " to bow to me, but saw that" + " I was already bowing to" + " him.%n",
          this.name, bower.getName());
    }
  }

  public void bowBack(LockedFriend bower) {
    System.out.format("%s: %s has" + " bowed back to me!%n", this.name, bower.getName());
  }
}
