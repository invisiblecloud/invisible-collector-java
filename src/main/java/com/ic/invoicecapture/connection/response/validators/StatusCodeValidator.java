package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.ServerResponse;
import com.ic.invoicecapture.exceptions.RequestStatusException;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.javatuples.Pair;

public class StatusCodeValidator implements IResponseValidator {

  private ServerResponse responsePair;

  public StatusCodeValidator(ServerResponse responsePair) {
    this.responsePair = responsePair;
  }

  @Override
  public Pair<Boolean, RequestStatusException> validate() {
    StatusLine statusLine = responsePair.getStatusLine();
    final int statusCode = statusLine.getStatusCode();
    final int statusFamily = statusCode / 100;
    if (statusFamily != 2) {
      RequestStatusException statusException =
          new RequestStatusException(statusCode, statusLine.getReasonPhrase());
      try {
        final String body = EntityUtils.toString(this.responsePair.getBodyEntity());
        statusException.setDescription(body);
      } catch (Exception e) {
        statusException.setDescription("error parsing server message response");
      }
      return Pair.with(false, statusException);
    } else {
      return Pair.with(true, null);
    }
  }
}
