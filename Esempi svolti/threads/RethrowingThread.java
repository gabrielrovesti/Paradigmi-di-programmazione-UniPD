package it.unipd.pdp2021.threads;

import static java.lang.System.out;

import java.util.function.Supplier;

/** A supplier of threads that will rethrow InterruptedExceptions as RuntimeExceptions. */
class RethrowingThreadBuilder implements Supplier<Thread> {

  Supplier<Long> waitTime;

  public RethrowingThreadBuilder(long wait) {
    waitTime = () -> wait;
  }

  @Override
  public Thread get() {
    return new Thread(
        () -> {
          String s = Thread.currentThread().getName();
          long t = waitTime.get();
          out.println(s + " will wait for " + t + " ms.");
          try {
            Thread.sleep(t);
            out.println(s + " is done wating for " + t + " ms.");
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });
  }
}

/** A thread whose exceptions are managed */
public class RethrowingThread {

  public static void main(String[] args) {

    // A thread that runs for some time
    final Thread tgt = new RethrowingThreadBuilder(2000L).get();

    // We set an exception handler that will manage uncaught exceptions for this thread
    tgt.setUncaughtExceptionHandler(
        (Thread t, Throwable e) -> {
          out.println(
              "Thread " + t.getName() + " has thrown:\n" + e.getClass() + ": " + e.getMessage());
        });

    // A thread that will interrupt the target one
    final Thread interrupter = new Thread(new Interrupter(tgt));

    interrupter.start();
    tgt.start();
  }
}
