package it.unipd.pdp2021.sync;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/** Thread-safe buffer using Locks and Conditions */
class Buffer {
  private LinkedList<String> buffer;
  private int maxSize;
  private ReentrantLock lock;
  private Condition lines;
  private Condition space;
  private boolean pendingLines;

  /**
   * Prepare buffer area and locks.
   *
   * @param maxSize Buffer size
   */
  public Buffer(int maxSize) {
    this.maxSize = maxSize;
    buffer = new LinkedList<>();
    lock = new ReentrantLock();
    lines = lock.newCondition();
    space = lock.newCondition();
    pendingLines = true;
  }

  /**
   * Insert a line. Signal threads that were waiting for one.
   *
   * @param line Line to insert.
   */
  public void insert(String line) {
    lock.lock();
    try {
      while (buffer.size() == maxSize) {
        space.await();
      }
      buffer.offer(line);
      System.out.printf("%s: Inserted Line: %d\n", Thread.currentThread().getName(), buffer.size());
      lines.signalAll();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

  /**
   * Get a line, or wait for it to be available.
   *
   * @return
   */
  public Optional<String> get() {
    Optional<String> line = Optional.empty();
    lock.lock();
    try {
      while ((buffer.size() == 0) && (hasPendingLines())) {
        lines.await();
      }
      if (hasPendingLines()) {
        line = Optional.ofNullable(buffer.poll());
        System.out.printf("%s: Line Read: %d\n", Thread.currentThread().getName(), buffer.size());
        space.signalAll();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
    return line;
  }

  /**
   * There are lines
   *
   * @return True if there are lines in the buffer
   */
  public boolean hasPendingLines() {
    return pendingLines || buffer.size() > 0;
  }

  /**
   * Signal that there are available lines
   *
   * @param pendingLines value for the signal
   */
  public void setPendingLines(boolean pendingLines) {
    this.pendingLines = pendingLines;
  }
}
