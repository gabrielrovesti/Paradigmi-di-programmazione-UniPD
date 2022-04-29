package it.unipd.pdp2021.ex1;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/** Main class */
public class Main {

  public static void main(String[] args) throws InterruptedException {
    AtomicInteger eidx = new AtomicInteger(0);
    List<Element> program =
        Stream.of("4S","4T","3F","FCCoSp4","StSq3","4S+3T","4T+REP","3A+1Lo+3S","3Lo","3Lz","FCSSp4","ChSq1","CCoSp4")
            .map(s -> new Element(eidx.incrementAndGet(), s))
            .toList();
    
    // set up the rink
    BlockingQueue<Element> rink = new LinkedBlockingQueue<Element>(program); // TODO
    // set up the athlete
    Athlete hanyuYusuru = new Athlete(rink, program);

    // set up the rink video system and vote collection
    VideoSystem video = new VideoSystem(rink);
    
    BlockingQueue<Vote> votes = new LinkedBlockingQueue<Vote>(); // TODO
    
    // set up the judges and technical panel
    AtomicInteger jidx = new AtomicInteger(0);
    var judges =
        List.of(Nation.values()).stream()
            .map(n -> new Judge(n, jidx.incrementAndGet(), video.screen(), votes))
            .toList();

    var techPanel = new TechnicalPanel(rink, votes);
    
    // set up the score board
    var score = new ScoreBoard(votes);

    // set up the 1s printer
    var printer = new Printer(score);

    // start the athlete, the judges, the technical panel and the scoring board
    hanyuYusuru.start(); //parte l'atleta connesso ad un certo rink
    video.start();		 //mostra gli elementi ad ogni schermo, iniziando a registrare
    //techPanel.start();	 //guarda i valori presenti ed emette i base values
    int i=0;
    while(i < judges.size()) {
    	judges.get(i).start();	//guarda i valori presenti ed emette i Grade of Execution
    	i++;
    }
    techPanel.start();	 //guarda i valori presenti ed emette i base values
    score.start();		//inizio a sommare i voti
    // TODO: start what needs to be started
    printer.start();	//stampo
    
    // check if exercise and scoring is over
    int j=0;
    boolean check=false;
	do {
	  Thread.sleep(5000);
	  if(judges.get(j).done()) {
		  if(j == judges.size()-1)	check=true;
		  else j++;
	  }
      System.out.println("han: "+!hanyuYusuru.done());
      System.out.println("tech: "+!techPanel.done());
      System.out.println("vid:"+!video.done());
      System.out.println("Judge at: "+j+"is:"+judges.get(j).done());
	} while (!check || !hanyuYusuru.done() || !techPanel.done() || video.done()); // check if relevant components have stopped);
    // TODO: stop components that don't stop themselves
    score.stop();
    printer.stop();
  }
}