package it.unipd.pdp2021.actors;

import static java.lang.System.out;

import java.util.function.Function;

/** An effect is a change from one behaviour to another, after a message has been received */
public interface Effect<T> extends Function<Behavior<T>, Behavior<T>> {

  /**
   * Returns an effect that sets the next behaviour.
   *
   * @param next
   * @return
   */
  static <T> Effect<T> become(Behavior<T> next) {
    return current -> next;
  }

  /**
   * The effect of not changing behaviour
   *
   * @param <T>
   * @return The same behaviour as before
   */
  static <T> Effect<T> stay() {
    return old -> old;
  }

  /**
   * The effect of making the actor inactive.
   *
   * @param <T>
   * @return
   */
  static <T> Effect<T> die() {
    return become(
        msg -> {
          out.println("Dropping msg [" + msg + "]: the actor is inactive.");
          return stay();
        });
  }
}
