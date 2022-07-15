package it.unipd.pdp2021.sync;

import static it.unipd.pdp2021.sync.LockedBuffer.randomWait;

/** Reads from the source and inserts in the buffer */
class Producer implements Runnable {
  private CharSource source;
  private Buffer buffer;

  public Producer(CharSource source, Buffer buffer) {
    this.source = source;
    this.buffer = buffer;
  }

  @Override
  public void run() {
    buffer.setPendingLines(true);
    while (source.hasMoreLines())
      source
          .getLine()
          .ifPresent(
              (line) -> {
                buffer.insert(line);
                randomWait(50);
              });
    buffer.setPendingLines(false);
  }
}
