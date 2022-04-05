package it.unipd.pdp2021.sync;

/** Reads a line from the buffer and ponders on it. */
class Consumer implements Runnable {
  private Buffer buffer;

  public Consumer(Buffer buffer) {
    this.buffer = buffer;
  }

  @Override
  public void run() {
    while (buffer.hasPendingLines()) buffer.get().ifPresent((line) -> process(line));
  }

  private void process(String line) {
    LockedBuffer.randomWait(250);
  }
}
