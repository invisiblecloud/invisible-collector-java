package com.ic.invoicecapture.connection.request;

import com.ic.invoicecapture.connection.response.ServerResponse;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.IOException;

public interface IMessageExchanger {
  ServerResponse exchangeMessages() throws IOException, IcException;
}
