package com.ic.invoicecapture.connection.request;

import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.exceptions.IcException;

public interface IMessageExchanger {
  ServerResponseFacade exchangeMessages() throws IcException;
}
