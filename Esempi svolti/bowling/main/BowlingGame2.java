package it.unipd.pdp2021.bowling;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface Score {
  default Stream<Score> pins(int pins) {
    return Stream.of(this);
  }

  default boolean done() {
    return false;
  }

  default Score next(int frame) {
    return frame < 8 ? new Empty(frame + 1) : new LastFrame();
  }
}
;

interface DoneScore extends Score {
  @Override
  default boolean done() {
    return true;
  }

  int score();
}

record Empty(int frame) implements Score {
  @Override
  public Stream<Score> pins(int pins) {
    return pins < 10 ? Stream.of(new One(frame, pins)) : Stream.of(new Strike(frame), next(frame));
  }
}
;

record LastFrame() implements Score {
  @Override
  public Stream<Score> pins(int pins) {
    return Stream.of(new LastFrameOne(pins));
  }
}

record LastFrameOne(int first) implements Score {
  @Override
  public Stream<Score> pins(int pins) {
    return Stream.of(
        (first == 10 || first + pins == 10)
            ? new LastFrameLong(first + pins)
            : new LastFrameDone(first + pins));
  }
}

record LastFrameLong(int first) implements Score {
  @Override
  public Stream<Score> pins(int pins) {
    return Stream.of(new LastFrameDone(first + pins));
  }
}

record LastFrameDone(int total) implements DoneScore {
  @Override
  public int score() {
    return total;
  }
}
;

record One(int frame, int first) implements Score {
  @Override
  public Stream<Score> pins(int pins) {
    return first + pins < 10
        ? Stream.of(new Frame(frame, first + pins), next(frame))
        : Stream.of(new Spare(frame), next(frame));
  }
}
;

record Frame(int frame, int total) implements DoneScore {
  @Override
  public int score() {
    return total;
  }
}
;

record Spare(int frame) implements Score {
  @Override
  public Stream<Score> pins(int pins) {
    return Stream.of(new SpareTotal(frame, pins));
  }
}
;

record SpareTotal(int frame, int pins) implements DoneScore {
  @Override
  public int score() {
    return 10 + pins;
  }
}
;

record Strike(int frame) implements Score {
  @Override
  public Stream<Score> pins(int pins) {
    return Stream.of(new StrikePlus(frame, pins));
  }
}
;

record StrikePlus(int frame, int first) implements Score {
  @Override
  public Stream<Score> pins(int pins) {
    return Stream.of(new StrikeTotal(frame, first + pins));
  }
}
;

record StrikeTotal(int frame, int pins) implements DoneScore {
  @Override
  public int score() {
    return 10 + pins;
  }
}
;

class Status {

  List<DoneScore> completed = new ArrayList<>();
  List<Score> current = List.of(new Empty(0));

  void add(int pins) {
    Map<Boolean, List<Score>> next =
        current.stream().flatMap(c -> c.pins(pins)).collect(Collectors.groupingBy(Score::done));
    next.getOrDefault(Boolean.TRUE, List.of()).stream()
        .map(s -> (DoneScore) s)
        .forEachOrdered(completed::add);
    current = next.getOrDefault(Boolean.FALSE, List.of());
  }

  int score() {
    return completed.stream().mapToInt(DoneScore::score).sum();
  }
}

public class BowlingGame2 {

  Status status;

  public BowlingGame2(String frames) {
    status = new Status();
    for (int i : BowlingGame.read(frames)) status.add(i);
  }

  public int score() {
    return status.score();
  }
}
