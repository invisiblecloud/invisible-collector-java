package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import java.net.URI;

public class IcApiFacade {
  
  public static final URI PRODUCTION_BASE_URL = URI.create("https://api.invisiblecollector.com");
  private CompanyApiFacade companyFacade;
  private CustomerApiFacade customerFacade;
  private DebtApiFacade debtFacade;

  public IcApiFacade(String apiToken) {
    this(apiToken, PRODUCTION_BASE_URL);
  }

  public IcApiFacade(String apiToken, URI baseUrl) {
    this.companyFacade = new CompanyApiFacade(apiToken, baseUrl);
    this.customerFacade = new CustomerApiFacade(apiToken, baseUrl);
    this.debtFacade = new DebtApiFacade(apiToken, baseUrl);
  }

  public IcApiFacade(ApiRequestFacade apiFacade) {
    this.companyFacade = new CompanyApiFacade(apiFacade);
    this.customerFacade = new CustomerApiFacade(apiFacade);
    this.debtFacade = new DebtApiFacade(apiFacade);
  }

  public CompanyApiFacade getCompanyFacade() {
    return companyFacade;
  }

  public CustomerApiFacade getCustomerFacade() {
    return customerFacade;
  }

  public DebtApiFacade getDebtFacade() {
    return debtFacade;
  }
}
