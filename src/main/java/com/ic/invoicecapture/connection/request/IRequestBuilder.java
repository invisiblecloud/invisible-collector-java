package com.ic.invoicecapture.connection.request;

import org.apache.http.client.methods.HttpUriRequest;

public interface IRequestBuilder {
  HttpUriRequest build();
}
