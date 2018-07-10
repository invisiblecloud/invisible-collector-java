package com.ic.invoicecapture.connection.request;

import org.apache.http.client.methods.HttpUriRequest;

public interface IExchangerBuilder {
  IMessageExchanger build(HttpUriRequest request);
}
