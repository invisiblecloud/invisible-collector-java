package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.json.JsonFacade;
import com.ic.invoicecapture.model.Company;
import java.io.InputStream;
import java.net.URI;

/**
 * Thread safe.
 * @author ros
 *
 */
public class IcFacade {

  public static final URI PRODUCTION_BASE_URL = URI.create("https://api.invisiblecollector.com");
  public static final String COMPANIES_ENDPOINT = "companies";
  
  private ApiRequestFacade apiFacade;
  private JsonFacade jsonFacade;

  public IcFacade(String apiToken) {
    this.apiFacade = new ApiRequestFacade(apiToken, PRODUCTION_BASE_URL);
    this.jsonFacade = new JsonFacade(); 
  }
  
  public IcFacade(String apiToken, URI baseUrl) {
    this.apiFacade = new ApiRequestFacade(apiToken, baseUrl);
    this.jsonFacade = new JsonFacade(); 
  }
  
  public IcFacade(ApiRequestFacade apiFacade) {
    this.apiFacade = apiFacade;
    this.jsonFacade = new JsonFacade();
  }
  
  public IcFacade(ApiRequestFacade apiFacade, JsonFacade jsonFacade) {
    this.apiFacade = apiFacade;
    this.jsonFacade = jsonFacade;
  }

  public Company requestCompanyInfo() throws IcException {
    InputStream inputStream = apiFacade.getRequest(COMPANIES_ENDPOINT);

    return this.jsonFacade.stringStreamToJsonObject(inputStream, Company.class);
  }

}
