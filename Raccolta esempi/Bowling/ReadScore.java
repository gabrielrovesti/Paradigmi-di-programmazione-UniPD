import java.util.List;
import org.junit.params.ParameterizedTest;
import org.junit.params.provider.Arguments;
import org.junit.params.provider.MethodSource;

public class readScore{
    public static List<Arguments> readScore(){
        return List.of(
            arguments(
            "1234__________________________";)
        )
    }

    @ParameterizedTest
    @MethodSource
    public void readScore(String score, int result){
        assertEquals(result, new BowlingGame(score).score());
        assertEquals(result, new BowlingGame2(score).score());
    }
}

