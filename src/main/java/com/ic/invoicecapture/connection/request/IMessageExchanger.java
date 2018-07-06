package com.ic.invoicecapture.connection.request;

import com.ic.invoicecapture.connection.response.ServerResponse;
import java.io.IOException;

public interface IMessageExchanger {
  ServerResponse exchangeMessages() throws IOException;
}
