import java.util.List;
import org.junit.params.ParameterizedTest;
import org.junit.params.provider.Arguments;
import org.junit.params.provider.MethodSource;

public class Games{
    public static List<Arguments> Games(){
        return List.of(
            arguments("1/35XXX458/X3/23", 160),
            arguments("1/35XXX458/X3/XX6", 189),
            arguments("1/35XXX458/X357/6", 164),
            arguments("1/35XXX458/X3/X43", 180),
            arguments("XXXXXXXXXXXXX", 300),
            arguments("XXXXXXXXXXX12", 274),
            arguments("1/35XXX458/X3/123", 157),
            arguments("5/5/5/5/5/5/5/5/5/5/5", 150),
            arguments("9/8/--9-9-9-9-9-9-9-1", 84),
            arguments("9-X8-9-9-9-9-9-9-X23", 104));
    }

    @ParameterizedTest(name = "{index}: {0} -> {1}")
    @MethodSource
    public void games(String score, int result){
        assertEquals(result, new BowlingGame(score).score());
        assertEquals(result, new BowlingGame2(score).score());
    }
}