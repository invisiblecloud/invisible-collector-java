package com.invisiblecollector;

import com.invisiblecollector.connection.ApiRequestFacade;
import com.invisiblecollector.connection.builders.IThrowingBuilder;
import com.invisiblecollector.connection.response.validators.IValidator;
import com.invisiblecollector.connection.response.validators.ValidatorBuilder;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.Company;
import com.invisiblecollector.model.CompanyField;
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
    ValidatorBuilder builder = this.validatorBuilder.clone();
    return this.returningRequest(Company.class, builder,
        (validator) -> apiFacade.getRequest(validator, COMPANIES_ENDPOINT));
  }

  /**
   * Enable or disable notifications for the company's customer.
   * 
   * @param enableNotifications true: enable notifications, false: disable notifications
   * @return up-to-date company info.
   * @throws IcException on any general exception
   */
  public Company setCompanyNotifications(boolean enableNotifications) throws IcException {
    IThrowingBuilder<InputStream, IValidator> requestMethod = enableNotifications
        ? (validator) -> apiFacade.putRequest(validator, ENABLE_NOTIFICATIONS_ENDPOINT, null)
        : (validator) -> apiFacade.putRequest(validator, DISABLE_NOTIFICATIONS_ENDPOINT, null);

    ValidatorBuilder builder = this.validatorBuilder.clone();
    return this.returningRequest(Company.class, builder, requestMethod);
  }


  /**
   * Update company info.
   * 
   * <p>You should use {@link #requestCompanyInfo()} before using this method to request company 
   * info since the name and vatNumber mandatory
   * company fields cannot be changed and are needed for validation and consistency purposes.
   * 
   * @param companyInfo the company info. name and vatNumber are <b>mandatory</b> attributes.
   * @return up-to-date company info
   * @throws IcException on any general exception
   */
  public Company updateCompanyInfo(Company companyInfo) throws IcException {
    companyInfo.assertConstainsKeys("name", "vatNumber");
    ValidatorBuilder builder = this.validatorBuilder.clone().addBadClientJsonValidator();
    String jsonToSend = this.jsonFacade.toJson(companyInfo);
    return this.returningRequest(Company.class, builder,
        (validator) -> apiFacade.putRequest(validator, COMPANIES_ENDPOINT, jsonToSend));
  }
  
}
