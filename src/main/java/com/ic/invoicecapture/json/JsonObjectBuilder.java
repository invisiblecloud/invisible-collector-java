package com.ic.invoicecapture.json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.javatuples.Pair;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ic.invoicecapture.connection.IRequest;
import com.ic.invoicecapture.exception.RequestException;

public class JsonObjectBuilder {

  private IRequest request;

  public JsonObjectBuilder(IRequest request) {
    this.request = request;
  }

  private Pair<Boolean, RequestException> isResponseStatusOk(StatusLine statusLine) {
    final int statusCode = statusLine.getStatusCode();
    final int statusFamily = statusCode / 100;
    if (statusFamily != 2) {
      RequestException e = new RequestException(statusCode, statusLine.getReasonPhrase());
      return Pair.with(true, e);
    } else {
      return Pair.with(false, null);
    }
  }

  public JsonObject build() throws IOException, RequestException {
    Pair<StatusLine, HttpEntity> response = request.exchangeMessages();
    StatusLine statusLine = response.getValue0();
    HttpEntity body = response.getValue1();
    String bodyString = EntityUtils.toString(body, StandardCharsets.UTF_8);
    Pair<Boolean, RequestException> responseValidity = isResponseStatusOk(statusLine);
    if (! responseValidity.getValue0()) {
      RequestException e = responseValidity.getValue1();
      e.setDescription(bodyString);
      throw e;
    }

    JsonObject jsonObject = new JsonParser().parse(bodyString).getAsJsonObject();
    return jsonObject;
  }
}
