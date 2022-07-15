package it.unipd.pdp2021.safe;

import java.util.List;

public class ListUpdater implements Runnable {

  List<String> list;

  ListUpdater(List<String> list) {
    this.list = list;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(300);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    list.add("d");
  }
}
