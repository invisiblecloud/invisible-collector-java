package com.ic.invoicecapture.connection.request;

import com.ic.invoicecapture.connection.response.ServerResponse;
import com.ic.invoicecapture.exceptions.IcException;

public interface IMessageExchanger {
  ServerResponse exchangeMessages() throws IcException;
}
