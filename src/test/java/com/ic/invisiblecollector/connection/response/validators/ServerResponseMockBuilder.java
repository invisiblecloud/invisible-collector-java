package com.ic.invisiblecollector.connection.response.validators;

import com.ic.invisiblecollector.connection.response.IServerResponse;
import com.ic.invisiblecollector.exceptions.IcException;
import java.util.ArrayList;
import java.util.List;
import org.easymock.EasyMock;
import org.javatuples.Pair;

public class ServerResponseMockBuilder {

  private int statusCode = 200;
  private String reason = "OK";
  private String body = "{}";
  List<Pair<String, String>> headers = new ArrayList<>();

  public IServerResponse build() throws IcException {
    IServerResponse responseMock = EasyMock.createNiceMock(IServerResponse.class);
    EasyMock.expect(responseMock.getStatusCode()).andReturn(statusCode);
    EasyMock.expect(responseMock.getStatusCodeReasonPhrase()).andReturn(reason);
    EasyMock.expect(responseMock.consumeConnectionAsString()).andReturn(body);

    for (Pair<String, String> header : headers) {
      EasyMock.expect(responseMock.getHeaderValues(header.getValue0()))
          .andReturn(header.getValue1());
    }

    if (headers.size() == 0) {
      EasyMock.expect(responseMock.getHeaderValues(EasyMock.anyString()))
          .andReturn("");
    }

    EasyMock.replay(responseMock);

    return responseMock;
  }

  public ServerResponseMockBuilder addHeader(String name, String value) {
    headers.add(Pair.with(name, value));
    return this;
  }

  public ServerResponseMockBuilder setStatusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public ServerResponseMockBuilder setReason(String reason) {
    this.reason = reason;
    return this;
  }

  public ServerResponseMockBuilder setBody(String body) {
    this.body = body;
    return this;
  }


}
