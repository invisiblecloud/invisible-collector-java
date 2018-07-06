package com.ic.invoicecapture.connection.request;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.javatuples.Pair;
import com.ic.invoicecapture.connection.response.ServerResponse;

public interface IMessageExchanger {
  public ServerResponse exchangeMessages() throws IOException, ClientProtocolException;
}
