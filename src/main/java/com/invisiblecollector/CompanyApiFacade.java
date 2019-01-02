package com.invisiblecollector;

import com.invisiblecollector.connection.ApiRequestFacade;
import com.invisiblecollector.connection.builders.ThrowingSupplier;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.Company;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

/**
 * Immutable and thread safe class for making operations on the {@code /companies } API endpoint.
 *
 * <p>For object construction see {@link IcApiFacade}
 *
 * @author ros
 */
public class CompanyApiFacade extends ApiBase {

  private static final String COMPANIES_ENDPOINT = "companies";
  private static final String DISABLE_NOTIFICATIONS_ENDPOINT = "companies/disableNotifications";
  private static final String ENABLE_NOTIFICATIONS_ENDPOINT = "companies/enableNotifications";

  public CompanyApiFacade(String apiToken, URI baseUrl) {
    super(apiToken, baseUrl);
  }

  public CompanyApiFacade(ApiRequestFacade apiFacade) {
    super(apiFacade);
  }

  /**
   * Request the company info from the database.
   *
   * @return up-to-date company info.
   * @throws IcException on any general exception
   */
  public Company requestCompanyInfo() throws IcException {
    return this.returningRequest(Company.class, () -> apiFacade.getRequest(COMPANIES_ENDPOINT));
  }

  /**
   * Enable or disable notifications for the company's customer.
   *
   * @param enableNotifications true: enable notifications, false: disable notifications
   * @return up-to-date company info.
   * @throws IcException on any general exception
   */
  public Company setCompanyNotifications(boolean enableNotifications) throws IcException {
    ThrowingSupplier<InputStream, IcException> requestMethod =
        enableNotifications
            ? () -> apiFacade.putRequest(ENABLE_NOTIFICATIONS_ENDPOINT, null)
            : () -> apiFacade.putRequest(DISABLE_NOTIFICATIONS_ENDPOINT, null);

    return this.returningRequest(Company.class, requestMethod);
  }

  /**
   * Update company info.
   *
   * <p>You should use {@link #requestCompanyInfo()} before using this method to request company
   * info since the name and vatNumber mandatory company fields are needed for
   * validation and consistency purposes.
   *
   * @param companyInfo the company info. name and vatNumber are <b>mandatory</b> attributes.
   * @return up-to-date company info
   * @throws IcException on any general exception
   */
  public Company updateCompanyInfo(Company companyInfo) throws IcException {
    companyInfo.assertConstainsKeys("name", "vatNumber");
    Map<String, Object> company =
        companyInfo.getOnlyFields("name", "vatNumber", "address", "zipCode", "city");
    String jsonToSend = this.jsonFacade.toJson(company);
    return this.returningRequest(
        Company.class, () -> apiFacade.putRequest(COMPANIES_ENDPOINT, jsonToSend));
  }
}
