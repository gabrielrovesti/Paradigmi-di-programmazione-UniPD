import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BowlingJava{

    List<Integer> pins;
    String score;

    public BowlingJava(String score){
        this.pins = read(score);
        this.score = score;
    }

    public int score(){
        int res = 0;
        int i = 0, frame = 0;
        boolean second = false;

        while(frame < 9){
            int curr = pins.get(i++);
            res += curr;

            if(score.charAt(i) == 'X' && (i < pins.size() - 2)){
                res += pins.get(i + 1) + pins.get(i + 2);
                frame++;
            }
            else if (second){
                if(score.charAt(i) == '/' && (i < pins.size() - 2)){
                res += pins.get(i + 1) + pins.get(i + 2);
                second = false;
                frame++;
                }
            }
            else{
                second = true;
            }
            i++;
        }

        for(; i < pins.size(); i++){

        }
        

        for(int i = 0; i < pins.size(); i++){
            res += pins.get(i);
            if(score.charAt(i) == '/' && (i < pins.size() - 2))
                res += pins.get(i + 1);
            if(score.charAt(i) == 'X' && (i < pins.size() - 2))
                res += pins.get(i + 1) + pins.get(i + 2);
        }

        return res;
    }

    static List<Integer> read(String score){
        List<Integer> res = new ArrayList<>();
        for(int i = 0; i < score.length(); i++){
            var ch = score.charAt(i);
            switch(ch){
                case '-' : res.add(0); break;
                case '/' : res.add(score.charAt(i-1) - '0'); break;
                case 'X': res.add(10); break;
                default: res.add(ch - '0');
            }
        }
        return res;
    }
}