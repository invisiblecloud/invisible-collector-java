package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Company;
import com.ic.invoicecapture.model.ICompanyUpdate;
import com.ic.invoicecapture.model.json.JsonModelFacade;
import java.io.InputStream;
import java.net.URI;

/**
 * Thread safe.
 * 
 * @author ros
 *
 */
public class IcFacade {

  public static final URI PRODUCTION_BASE_URL = URI.create("https://api.invisiblecollector.com");
  public static final String COMPANIES_ENDPOINT = "companies";
  public static final String ENABLE_NOTIFICATIONS_ENDPOINT = "companies/enableNotifications";
  public static final String DISABLE_NOTIFICATIONS_ENDPOINT = "companies/disableNotifications";


  private ApiRequestFacade apiFacade;
  private JsonModelFacade jsonFacade;

  public IcFacade(String apiToken) {
    this(new ApiRequestFacade(apiToken, PRODUCTION_BASE_URL));
  }

  public IcFacade(String apiToken, URI baseUrl) {
    this(new ApiRequestFacade(apiToken, baseUrl));
  }

  public IcFacade(ApiRequestFacade apiFacade) {
    this(apiFacade, new JsonModelFacade());
  }

  public IcFacade(ApiRequestFacade apiFacade, JsonModelFacade jsonFacade) {
    this.apiFacade = apiFacade;
    this.jsonFacade = jsonFacade;
  }

  public Company requestCompanyInfo() throws IcException {
    InputStream inputStream = apiFacade.getRequest(COMPANIES_ENDPOINT);

    return this.jsonFacade.parseStringStream(inputStream, Company.class);
  }

  public Company updateCompanyInfo(ICompanyUpdate companyInfo) throws IcException {
    String jsonToSend = this.jsonFacade.toJson(companyInfo);

    InputStream inputStream = apiFacade.putRequest(COMPANIES_ENDPOINT, jsonToSend);
    return this.jsonFacade.parseStringStream(inputStream, Company.class);
  }

  public Company setCompanyNotifications(boolean bEnableNotifications) throws IcException {
    InputStream inputStream;
    if (bEnableNotifications) {
      inputStream = apiFacade.putRequest(ENABLE_NOTIFICATIONS_ENDPOINT, null);
    } else {
      inputStream = apiFacade.putRequest(DISABLE_NOTIFICATIONS_ENDPOINT, null);
    }

    return this.jsonFacade.parseStringStream(inputStream, Company.class);
  }


}
