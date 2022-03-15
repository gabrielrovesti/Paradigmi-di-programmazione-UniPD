import java.util.List;
import org.junit.params.ParameterizedTest;
import org.junit.params.provider.Arguments;
import org.junit.params.provider.MethodSource;

public class Strikes{
    public static List<Arguments> strikes(){
        return List.of(
            arguments("11X___________________", 12),
            arguments("11X34___________________", 26),
            arguments("11XX34_________________", 49),
            arguments("11XX34/X8/4____________", 107),
        ));
    }

    @ParameterizedTest
    @MethodSource
    public void strikes(String score, int result){
        assertEquals(result, new BowlingGame(score).score());
        assertEquals(result, new BowlingGame2(score).score());
    }
}