package it.unipd.pdp2021.actors;

import java.util.function.Function;

/** A behaviour consumes a message and returns a state transition. */
public interface Behavior<T> extends Function<T, Effect<T>> {}
