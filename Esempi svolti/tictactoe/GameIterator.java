package it.unipd.app2020.tictactoe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GameIterator implements Spliterator<Game> {

  Queue<Game> current;

  public GameIterator(Game seed) {
    current = new LinkedList<Game>(seed.moves());
  }

  @Override
  public boolean tryAdvance(Consumer<? super Game> action) {
    boolean result = false;
    if (current.isEmpty()) {
      // nothing more available
      result = false;
    } else {
      // a ready element
      Game res = current.remove();
      current.addAll(res.moves());
      action.accept(res);
      result = true;
    }
    return result;
  }

  @Override
  public Spliterator<Game> trySplit() {
    Spliterator<Game> res = null;
    if (current.size() > 1) {
      List<Game> dest = new ArrayList<>();
      int len = current.size() / 2;
      for (int i = 0; i <= len; i++) dest.add(current.remove());
      res = new GameIterator(dest.iterator());
    }
    return res;
  }

  private GameIterator(Iterator<Game> current) {
    this.current = new LinkedList<Game>();
    while (current.hasNext()) this.current.offer(current.next());
  }

  @Override
  public long estimateSize() {
    return -1;
  }

  @Override
  public int characteristics() {
    return Spliterator.IMMUTABLE | Spliterator.DISTINCT | Spliterator.NONNULL;
  }
}
