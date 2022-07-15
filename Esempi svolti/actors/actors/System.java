package it.unipd.pdp2021.actors;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/** The actor system */
public record System(Executor executor) {

  /**
   * Build an actor from a creator function
   *
   * @param <T>
   * @param initial A function that provides the initial state
   * @return The reference to the new actor
   */
  public <T> Address<T> actorOf(Function<Address<T>, Behavior<T>> initial) {

    /** A local class: encapsulates the status of the actor. */
    abstract class AtomicRunnableAddress<K> implements Address<K>, Runnable {
      // the actor's status: if 0, the actor is consuming a message.
      AtomicInteger on = new AtomicInteger(0);
    }

    /** A new actor instance is built, providing the interface implementations. */
    var addr =
        new AtomicRunnableAddress<T>() {

          // The actor's mailbox
          final ConcurrentLinkedQueue<T> mbox = new ConcurrentLinkedQueue<>();

          // Current behaviour
          Behavior<T> behavior= initial.apply(this);

          // implementation of message reception
          public Address<T> tell(T msg) {
            mbox.offer(msg);
            async();
            return this;
          }

          // If we are active, process a message
          public void run() {
            try {
              if (on.get() == 1) behavior = behavior.apply(mbox.poll()).apply(behavior);
            } finally {
              // processing complete, the actor is inactive
              on.set(0);
              async();
            }
          }

          // Process a message
          void async() {
            // If there is a message, and we are not active
            if (!mbox.isEmpty() && on.compareAndSet(0, 1)) {
              // Let's schedule the execution of the message
              try {
                executor.execute(this);
              } catch (Throwable t) {
                // An error happened: this actor is deactivated
                on.set(0);
                throw t;
              }
            }
          }

        };

    return addr;
  }
}
