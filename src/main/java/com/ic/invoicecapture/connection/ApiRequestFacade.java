package com.ic.invoicecapture.connection;

import com.ic.invoicecapture.model.Company;

public class ApiRequestFacade {
  private String xApiToken;
  private String baseUrl;

  public ApiRequestFacade(String xApiToken, String baseUrl) {
    this.xApiToken = xApiToken;
    this.baseUrl = baseUrl;
  }

  public Company getCompanyInformation() {
    return null; //TODO
  }
}
