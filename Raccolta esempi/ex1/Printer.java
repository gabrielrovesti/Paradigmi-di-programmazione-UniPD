package it.unipd.pdp2021.ex1;

import java.util.concurrent.TimeUnit;

/** Prints the partial score at regular intervals. */
public class Printer implements Runnable {

  private final ScoreBoard score;
  private boolean stop;
  private Thread printing;

  Printer(ScoreBoard score) {
    this.score = score;
    printing = new Thread(this);
  }

  @Override
  public void run() {
    try {
      while (!stop) {
        // TODO: Print the result. Wait 1 second.
    	  TimeUnit.SECONDS.sleep(1);
    	  if(score.result != null) {
    		  System.out.println(ScoreBoard.format(score.result.get().elements(),score.result.get().total()));
    	  }
      }
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
  }

  /** Starts the printer */
  void start() {
    this.printing.start();
  }

  /** Asks the printer to stop */
  void stop() {
    this.stop = true;
  }
}