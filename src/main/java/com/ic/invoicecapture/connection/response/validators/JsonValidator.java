package com.ic.invoicecapture.connection.response.validators;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.javatuples.Pair;
import com.ic.invoicecapture.exceptions.RequestStatusException;
import com.ic.invoicecapture.connection.request.IMessageExchanger;
import com.ic.invoicecapture.exceptions.BadContentTypeException;

public class JsonValidator implements IResponseValidator {

  private HttpEntity bodyEntity;

  public JsonValidator(HttpEntity bodyEntity) {
    this.bodyEntity = bodyEntity;
  }

  private static final String JSON_CONTENT_TYPE = "application/json";

  @Override
  public Pair<Boolean, BadContentTypeException> validate() {
    final String contentType = this.bodyEntity.getContentType().getValue();

    if (!contentType.contains(JSON_CONTENT_TYPE)) {
      return Pair.with(true, null);
    } else {
      BadContentTypeException e = new BadContentTypeException(contentType, JSON_CONTENT_TYPE);
      return Pair.with(false, e);
    }
  }
}
