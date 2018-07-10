package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.exceptions.IcException;
import org.apache.http.HttpEntity;

public class JsonValidator implements IValidator {

  private static final String JSON_CONTENT_TYPE = "application/json";

  private String contentType;

  public JsonValidator(HttpEntity bodyEntity) {
    this.contentType = bodyEntity.getContentType().getValue();
  }

  public JsonValidator(String contentType) {
    this.contentType = contentType;
  }

  @Override
  public ValidationResult validate() {
    if (this.contentType.contains(JSON_CONTENT_TYPE)) {
      return ValidationResult.buildPassing();
    } else {
      final String exceptionMessage = "Wrong content-type received, expected: " + this.contentType
          + ", received: " + JSON_CONTENT_TYPE;
      IcException e = new IcException(exceptionMessage);
      return new ValidationResult(false, e);
    }
  }
}
