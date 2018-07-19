package com.ic.invoicecapture;

import java.net.URI;

public class IcApiFacade {
  
  public static final URI PRODUCTION_BASE_URL = URI.create("https://api.invisiblecollector.com");
  private CompanyApiFacade companyFacade;
  private CustomerApiFacade customerFacade;

  public IcApiFacade(String apiToken) {
    this(apiToken, PRODUCTION_BASE_URL);
  }

  public IcApiFacade(String apiToken, URI baseUrl) {
    this.companyFacade = new CompanyApiFacade(apiToken, baseUrl);
    this.customerFacade = new CustomerApiFacade(apiToken, baseUrl);
  }

  public CompanyApiFacade getCompanyFacade() {
    return companyFacade;
  }

  public CustomerApiFacade getCustomerApiFacade() {
    return customerFacade;
  }

  public void setCompanyFacade(CompanyApiFacade companyFacade) {
    this.companyFacade = companyFacade;
  }

  public void setCustomerFacade(CustomerApiFacade customerFacade) {
    this.customerFacade = customerFacade;
  }
}
