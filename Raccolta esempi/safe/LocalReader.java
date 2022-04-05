package it.unipd.pdp2021.safe;

/** Reads a threadlocal variable */
class LocalReader implements Runnable {

  private final LocalVar var;
  private final int item;

  LocalReader(LocalVar var, int item) {
    this.var = var;
    this.item = item;
  }

  @Override
  public void run() {
    System.out.println(Thread.currentThread().getName() + ", item " + item + ": read " + var.get());
  }
}
