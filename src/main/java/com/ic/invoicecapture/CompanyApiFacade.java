package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import com.ic.invoicecapture.connection.builders.IThrowingBuilder;
import com.ic.invoicecapture.connection.response.validators.IValidator;
import com.ic.invoicecapture.connection.response.validators.ValidatorBuilder;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Company;
import com.ic.invoicecapture.model.CompanyField;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

/**
 * Immutable and Thread safe class. 
 * 
 * @author ros
 *
 */
public class CompanyApiFacade extends ApiBase {

  public static final String COMPANIES_ENDPOINT = "companies";
  public static final String DISABLE_NOTIFICATIONS_ENDPOINT = "companies/disableNotifications";
  public static final String ENABLE_NOTIFICATIONS_ENDPOINT = "companies/enableNotifications";

  public CompanyApiFacade(String apiToken, URI baseUrl) {
    super(apiToken, baseUrl);
  }

  public CompanyApiFacade(ApiRequestFacade apiFacade) {
    super(apiFacade);
  }

  public Company requestCompanyInfo() throws IcException {
    ValidatorBuilder builder = this.validatorBuilder.clone();
    return this.returningRequest(Company.class, builder,
        (validator) -> apiFacade.getRequest(validator, COMPANIES_ENDPOINT));
  }

  public Company setCompanyNotifications(boolean enableNotifications) throws IcException {
    IThrowingBuilder<InputStream, IValidator> requestMethod = enableNotifications
        ? (validator) -> apiFacade.putRequest(validator, ENABLE_NOTIFICATIONS_ENDPOINT, null)
        : (validator) -> apiFacade.putRequest(validator, DISABLE_NOTIFICATIONS_ENDPOINT, null);

    ValidatorBuilder builder = this.validatorBuilder.clone();
    return this.returningRequest(Company.class, builder, requestMethod);
  }

  public Company updateCompanyInfo(Company companyInfo) throws IcException {
    return updateCompanyInfo(companyInfo.toEnumMap());
  }

  public Company updateCompanyInfo(Map<CompanyField, Object> companyInfo) throws IcException {
    CompanyField.assertCorrectlyInitialized(companyInfo);
    ValidatorBuilder builder = this.validatorBuilder.clone().addBadClientJsonValidator();
    String jsonToSend = this.jsonFacade.toJson(companyInfo);
    return this.returningRequest(Company.class, builder,
        (validator) -> apiFacade.putRequest(validator, COMPANIES_ENDPOINT, jsonToSend));
  }
  
}
