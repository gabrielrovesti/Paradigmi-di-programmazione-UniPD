package it.unipd.pdp2021.safe;

import java.util.List;

public class ListTraverser implements Runnable {

  List<String> list;

  ListTraverser(List<String> list) {
    this.list = list;
  }

  @Override
  public void run() {
    list.iterator()
        .forEachRemaining(
            el -> {
              System.out.println(el);
              try {
                Thread.sleep(250);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });
  }
}
