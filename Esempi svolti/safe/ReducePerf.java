package it.unipd.pdp2021.safe;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

/** Sum two values waiting a little. */
class SumWait implements BiFunction<Long, Long, Long> {

  @Override
  public Long apply(Long a, Long b) {
    try {
      Thread.sleep(5);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return a + b;
  }
}

public class ReducePerf {

  /**
   * Measure (very empirically) the difference in efficency between a serial and a parallel
   * map-reduce.
   */
  public static void main(String[] args) {
    Random rnd = new Random();
    ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<String, Long>();
    IntStream.range(0, 10000).forEach(i -> map.put("k" + i, Long.valueOf(rnd.nextInt(1000))));

    System.out.println("Start");

    long start = System.currentTimeMillis();
    Long parres = map.reduceEntries(500, entry -> entry.getValue(), new SumWait());
    long partime = System.currentTimeMillis() - start;

    System.out.println("Change");

    start = System.currentTimeMillis();
    Long serres = map.reduceEntries(10000001, entry -> entry.getValue(), new SumWait());
    long sertime = System.currentTimeMillis() - start;

    System.out.println(
        "Parallel sum:" + parres + " in " + partime + "\nSerial sum:" + serres + " in " + sertime);
  }
}
