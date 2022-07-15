package it.unipd.pdp2021.bowling;

import java.util.List;

public class BowlingGame {
	List <Integer> pins;
	String score;
	public BowlingGame(String score) {
		this.pins=read(score);
		this.score=score;
	}
	public int score() {
		int res=0, i=0,frame=0;
		boolean second=false;
		while(frame < 9) {
			if(score.charAt(i) == 'X' && i < pins.size() -2) {
				res += pins.get(i+1) + pins.get(i+2);
				frame++;
			} else if (second) {
				if(score.charAt(i) == '/' && i < pins.size() -2) {
					res += pins.get(i+1);
					second=false;
					frame++;
				}
			}
			else {
				second=true;
			}
			i++;
		}
		return res;
	}
  static List<Integer> read(String score) {
    return List.of();
  }
}
