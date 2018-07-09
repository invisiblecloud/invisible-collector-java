package com.ic.invoicecapture.exceptions;

public class RequestStatusException extends IcIoException {
  private int statusCode;
  private String statusMessage;
  private String description;

  public RequestStatusException(int statusCode, String statusMessage) {
    this.statusCode = statusCode;
    this.statusMessage = statusMessage;

  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getMessage() {
    String message = "Status code returned: " + this.statusCode + " " + this.statusMessage;
    if (this.description != null) {
      message += "\n" + "Description: " + this.description;
    }

    return message;
  }

  @Override
  public String toString() {
    return this.getMessage();
  }
}
