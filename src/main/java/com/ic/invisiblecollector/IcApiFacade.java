package com.ic.invisiblecollector;

import java.net.URI;
import com.ic.invisiblecollector.connection.ApiRequestFacade;

/**
 * A Thread-Safe and Immutable Container for the various api InvisibleCollector operations.
 *  
 * <p>Used to split the method surface area along logical divisions.
 * 
 * <p>Use this as the entry point into the Library.
 * 
 * @author ros
 *
 */
public class IcApiFacade {

  public static final URI PRODUCTION_BASE_URL = URI.create("https://api.invisiblecollector.com");
  private CompanyApiFacade companyFacade;
  private CustomerApiFacade customerFacade;
  private DebtApiFacade debtFacade;

  /**
   * Creates an object with the default hostname.
   * 
   * @param apiToken the company's Api Token
   */
  public IcApiFacade(String apiToken) {
    this(apiToken, PRODUCTION_BASE_URL);
  }

  
  /**
   * Creates an object with a custom hostname or base path.
   * 
   * @param apiToken the company's Api Token
   * @param baseUrl the hostname, scheme and optionally base path for the connection
   */
  public IcApiFacade(String apiToken, URI baseUrl) {
    this.companyFacade = new CompanyApiFacade(apiToken, baseUrl);
    this.customerFacade = new CustomerApiFacade(apiToken, baseUrl);
    this.debtFacade = new DebtApiFacade(apiToken, baseUrl);
  }
  
  /**
   * Creates an IcApiFacade with a custom api request object. 
   * 
   * <p>Preferably use {@link #IcApiFacade(String)} or {@link #IcApiFacade(String, URI)}
   * 
   * @param apiFacade the custom api request facade 
   */
  public IcApiFacade(ApiRequestFacade apiFacade) {
    this.companyFacade = new CompanyApiFacade(apiFacade);
    this.customerFacade = new CustomerApiFacade(apiFacade);
    this.debtFacade = new DebtApiFacade(apiFacade);
  }

  
  /**
   * Creates an IcApiFacade with custom facades for the endpoints.
   * 
   * <p>Preferably use {@link #IcApiFacade(String)} or {@link #IcApiFacade(String, URI)}
   * 
   * @param companyFacade the handler for {@code /companies } the endpoint
   * @param customerFacade the handler for {@code /customers} the endpoint
   * @param debtFacade the handler for {@code /debts } the endpoint
   */
  public IcApiFacade(CompanyApiFacade companyFacade, CustomerApiFacade customerFacade,
      DebtApiFacade debtFacade) {
    this.companyFacade = companyFacade;
    this.customerFacade = customerFacade;
    this.debtFacade = debtFacade;
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
