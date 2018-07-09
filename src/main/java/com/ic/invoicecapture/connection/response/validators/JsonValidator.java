package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.exceptions.BadContentTypeException;
import org.apache.http.HttpEntity;
import org.javatuples.Pair;

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
