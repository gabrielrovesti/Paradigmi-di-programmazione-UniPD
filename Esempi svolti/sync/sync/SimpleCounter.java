package it.unipd.pdp2021.sync;

/** A simple interface to a counter. */
interface SimpleCounter {

  /** Increment the state */
  public void add();

  /**
   * Read the internal value of the Counter
   *
   * @return the actual value
   */
  public int getState();
}
