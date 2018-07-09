package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import com.ic.invoicecapture.exceptions.RequestStatusException;
import com.ic.invoicecapture.json.JsonConversion;
import com.ic.invoicecapture.model.Company;
import java.io.IOException;
import java.io.InputStream;

/**
 * Thread safe.
 * @author ros
 *
 */
public class IcFacade {

  private static final String PRODUCTION_BASE_URL = "https://api.invisiblecollector.com";
  private static final String COMPANIES_ENDPOINT = "companies";
  
  private ApiRequestFacade apiFacade;

  public IcFacade(String apiToken) {
    this.apiFacade = new ApiRequestFacade(apiToken, PRODUCTION_BASE_URL);
  }

  public Company requestCompanyInfo() throws RequestStatusException, IOException {
    InputStream inputStream = apiFacade.getRequest(COMPANIES_ENDPOINT);

    return JsonConversion.stringStreamToJsonObject(inputStream, Company.class);
  }

}
