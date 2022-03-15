import java.util.List;
import org.junit.params.ParameterizedTest;
import org.junit.params.provider.Arguments;
import org.junit.params.provider.MethodSource;

public class SimplePins{
    public static List<Arguments> simplePins(){
        return List.of(
            arguments("_______________________", 0),
            arguments("1234___________________", 10),
            arguments("12341231234123412341234", 50),
            arguments("22222222222222222222222", 40),
        ));
    }

    @ParameterizedTest
    @MethodSource
    public void simplePins(String score, int result){
        assertEquals(result, new BowlingGame(score).score());
        assertEquals(result, new BowlingGame2(score).score());
    }
}