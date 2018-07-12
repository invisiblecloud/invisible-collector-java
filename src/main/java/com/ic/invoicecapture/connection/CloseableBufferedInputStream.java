package com.ic.invoicecapture.connection;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Follows Decorator pattern.
 * @author ros
 *
 */
public class CloseableBufferedInputStream extends BufferedInputStream {

  private Closeable closeable;
  private InputStream inputStream;


  public CloseableBufferedInputStream(InputStream inputStream, Closeable closeable) {
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
