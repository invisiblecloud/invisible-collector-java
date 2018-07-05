package com.ic.invoicecapture.connection;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.javatuples.Pair;

public interface IRequest {
  public Pair<StatusLine, HttpEntity> exchangeMessages() throws IOException, ClientProtocolException;
}
