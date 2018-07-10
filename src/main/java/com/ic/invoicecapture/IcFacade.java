package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.json.JsonFacade;
import com.ic.invoicecapture.model.Company;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Thread safe.
 * @author ros
 *
 */
public class IcFacade {

  private static final URI PRODUCTION_BASE_URL = URI.create("https://api.invisiblecollector.com");
  private static final String COMPANIES_ENDPOINT = "companies";
  
  private ApiRequestFacade apiFacade;
  private JsonFacade jsonFacade;

  public IcFacade(String apiToken) {
    this.apiFacade = new ApiRequestFacade(apiToken, PRODUCTION_BASE_URL);
    this.jsonFacade = new JsonFacade(); 
  }
  
  public IcFacade(ApiRequestFacade apiFacade, JsonFacade jsonFacade) {
    this.apiFacade = apiFacade;
    this.jsonFacade = jsonFacade;
  }

  public Company requestCompanyInfo() throws IOException, URISyntaxException, IcException {
    InputStream inputStream = apiFacade.getRequest(COMPANIES_ENDPOINT);

    return this.jsonFacade.stringStreamToJsonObject(inputStream, Company.class);
  }

}
