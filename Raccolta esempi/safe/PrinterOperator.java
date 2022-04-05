package it.unipd.pdp2021.safe;

import java.util.stream.IntStream;

public class PrinterOperator {

  public static void main(String[] args) {
    Printer concurrent = new ConcurrentPrinter(8);
    Thread thread[] = new Thread[10];
    System.out.println("Preparing...");
    IntStream.range(0, 10)
        .forEach(
            (i) ->
                thread[i] =
                    new Thread(
                        () -> {
                          System.out.println("Queueing job " + i);
                          concurrent.printJob(new Object());
                          System.out.println("Job " + i + " queued");
                        }));
    System.out.println("Starting.");
    for (int i = 0; i < 10; i++) {
      thread[i].start();
    }
  }
}
