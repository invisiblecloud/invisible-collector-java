package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import com.ic.invoicecapture.connection.builders.IThrowingBuilder;
import com.ic.invoicecapture.connection.response.validators.IValidator;
import com.ic.invoicecapture.connection.response.validators.ValidatorFactory;
import com.ic.invoicecapture.exceptions.IcConflictingException;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Company;
import com.ic.invoicecapture.model.Customer;
import com.ic.invoicecapture.model.ICompanyUpdate;
import com.ic.invoicecapture.model.ICustomerUpdate;
import com.ic.invoicecapture.model.IRoutable;
import com.ic.invoicecapture.model.json.JsonModelFacade;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

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
  private ValidatorFactory validatorFactory;

  public IcFacade(String apiToken) {
    this(new ApiRequestFacade(apiToken, PRODUCTION_BASE_URL));
  }

  public IcFacade(String apiToken, URI baseUrl) {
    this(new ApiRequestFacade(apiToken, baseUrl));
  }

  public IcFacade(ApiRequestFacade apiFacade) {
    this(apiFacade, new JsonModelFacade());
  }

  public IcFacade(ApiRequestFacade apiFacade, JsonModelFacade jsonFacade,
      ValidatorFactory validatorFactory) {
    this.apiFacade = apiFacade;
    this.jsonFacade = jsonFacade;
    this.validatorFactory = validatorFactory;
  }

  private IcFacade(ApiRequestFacade apiFacade, JsonModelFacade jsonFacade) {
    this(apiFacade, jsonFacade, new ValidatorFactory());
  }

  private Company returningCompanyMethod(IThrowingBuilder<InputStream, IValidator> requestMethod)
      throws IcException {
    IValidator validator = this.validatorFactory.buildBasicValidator();
    InputStream inputStream = requestMethod.build(validator);

    return this.jsonFacade.parseStringStream(inputStream, Company.class);
  }

  public Company requestCompanyInfo() throws IcException {
    return this
        .returningCompanyMethod((validator) -> apiFacade.getRequest(validator, COMPANIES_ENDPOINT));
  }

  public Company updateCompanyInfo(ICompanyUpdate companyInfo) throws IcException {
    String jsonToSend = this.jsonFacade.toJson(companyInfo);
    return this.returningCompanyMethod(
        (validator) -> apiFacade.putRequest(validator, COMPANIES_ENDPOINT, jsonToSend));
  }

  public Company setCompanyNotifications(boolean enableNotifications) throws IcException {
    IThrowingBuilder<InputStream, IValidator> requestMethod = enableNotifications
        ? (validator) -> apiFacade.putRequest(validator, ENABLE_NOTIFICATIONS_ENDPOINT, null)
        : (validator) -> apiFacade.putRequest(validator, DISABLE_NOTIFICATIONS_ENDPOINT, null);

    return this.returningCompanyMethod(requestMethod);
  }

  public static final String CUSTOMERS_ENDPOINT = "customers";

  public Customer registerNewCustomer(ICustomerUpdate costumerInfo)
      throws IcException, IcConflictingException {
    String jsonToSend = this.jsonFacade.toJson(costumerInfo);
    IValidator validator = this.validatorFactory.buildConflictValidator();
    InputStream inputStream = apiFacade.postRequest(validator, CUSTOMERS_ENDPOINT, jsonToSend);

    return this.jsonFacade.parseStringStream(inputStream, Customer.class);
  }



  public Customer requestCustomerInfo(String customerId) throws IcException {
    String endpoint = CUSTOMERS_ENDPOINT + "/" + customerId;
    IValidator validator = this.validatorFactory.buildExistingEntityValidator();
    InputStream inputStream = apiFacade.getRequest(validator, endpoint);

    return this.jsonFacade.parseStringStream(inputStream, Customer.class);
  }

  public Customer updateCustomerInfo(ICustomerUpdate customerInfo, String customerId)
      throws IcException {
    String endpoint = CUSTOMERS_ENDPOINT + "/" + customerId;
    String json = this.jsonFacade.toJson(customerInfo);
    IValidator validator = this.validatorFactory.buildExistingConflictingEntityValidator();
    InputStream inputStream = apiFacade.putRequest(validator, endpoint, json);

    return this.jsonFacade.parseStringStream(inputStream, Customer.class);
  }

  private String getId(IRoutable idContainer) {
    String gid = idContainer.getGid();
    String externalId = idContainer.getExternalId();
    if (gid != null && !gid.isEmpty()) {
      return gid;
    } else if (externalId != null && !externalId.isEmpty()) {
      return externalId;
    } else {
      throw new IllegalArgumentException("no valid id contained in object");
    }
  }

  public Map<String, String> setCustomerAttributes(IRoutable idContainer,
      Map<String, String> attributes) throws IcException {
    String id = getId(idContainer);
    return setCustomerAttributes(id, attributes);
  }

  private static final String ATTRIBUTES_PATH = "attributes";

  public Map<String, String> setCustomerAttributes(String customerId,
      Map<String, String> attributes) throws IcException {
    String endpoint = String.join("/", CUSTOMERS_ENDPOINT, customerId, ATTRIBUTES_PATH);
    String jsonToSend = this.jsonFacade.toJson(attributes);
    IValidator validator = this.validatorFactory.buildConflictValidator();
    InputStream inputStream = apiFacade.postRequest(validator, endpoint, jsonToSend);

    return this.jsonFacade.parseStringStreamAsStringMap(inputStream);
  }

}
