package it.unipd.pdp2021.safe;

import java.util.concurrent.atomic.AtomicInteger;

/** Holds a threadlocal variable, initialized with a global, atomic counter. */
class LocalVar {

  // Atomic integer containing the next thread ID to be assigned
  private static final AtomicInteger nextId = new AtomicInteger(0);

  ThreadLocal<Integer> counter;

  LocalVar() {
    counter = ThreadLocal.withInitial(() -> nextId.incrementAndGet());
  }

  Integer get() {
    return counter.get();
  }
}
