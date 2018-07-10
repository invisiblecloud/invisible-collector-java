package com.ic.invoicecapture.connection.request;

import java.io.IOException;
import org.apache.http.HttpEntity;

public interface IEntityConsumer {
  void consume(HttpEntity entity) throws IOException;
}
