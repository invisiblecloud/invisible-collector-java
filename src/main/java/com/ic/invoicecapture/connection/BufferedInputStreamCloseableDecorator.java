package com.ic.invoicecapture.connection;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class BufferedInputStreamCloseableDecorator extends BufferedInputStream {

  private Closeable closeable;
  private InputStream inputStream;


  public BufferedInputStreamCloseableDecorator(InputStream inputStream, Closeable closeable) {
    super(inputStream);
    this.inputStream = inputStream;
    this.closeable = closeable;
  }

  
  @Override
  public void close() throws IOException {
    this.inputStream.close();
    this.closeable.close();
  }
}
