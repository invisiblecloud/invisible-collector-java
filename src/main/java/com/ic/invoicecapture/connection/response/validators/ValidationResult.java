package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.exceptions.IcException;

public class ValidationResult {
  
  private boolean isOk;
  private IcException exception;
  
  public ValidationResult(boolean isValid, IcException exception) {
    
    if (isValid && exception != null) {
      throw new IllegalArgumentException();
    } else if (!isValid && exception == null) {
      throw new IllegalArgumentException();
    }
    
    this.isOk = isValid;
    this.exception = exception; 
  }
  
  public static ValidationResult buildPassing() {
    return new ValidationResult(true, null);
  }
  
  public boolean isValid() {
    return this.isOk;
  }

  public void tryThrowException() throws IcException {
    if (! this.isValid()) {
      throw this.exception;
    }
  }

  public IcException getException() {
    return this.exception;
  }
}
