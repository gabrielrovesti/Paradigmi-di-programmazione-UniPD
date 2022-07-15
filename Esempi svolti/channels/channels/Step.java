package it.unipd.pdp2021.channels;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

abstract class Step<T> implements CompletionHandler<T, GameAttachment> {

  @Override
  public void failed(Throwable exc, GameAttachment attachment) {
    exc.printStackTrace();
  }

  static ByteBuffer wrap(byte[] bytes) {
    return ByteBuffer.wrap(bytes);
  }
}
