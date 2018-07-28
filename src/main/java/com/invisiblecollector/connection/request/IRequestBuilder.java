package com.invisiblecollector.connection.request;

import org.apache.http.client.methods.HttpUriRequest;

public interface IRequestBuilder {
  HttpUriRequest build();
}
