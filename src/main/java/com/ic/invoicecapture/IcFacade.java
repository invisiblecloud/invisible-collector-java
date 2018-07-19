package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import com.ic.invoicecapture.connection.builders.IThrowingBuilder;
import com.ic.invoicecapture.connection.response.validators.IValidator;
import com.ic.invoicecapture.connection.response.validators.ValidatorBuilder;
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

  public static final String ATTRIBUTES_PATH = "attributes";
  public static final String COMPANIES_ENDPOINT = "companies";
  public static final String CUSTOMERS_ENDPOINT = "customers";
  public static final String DISABLE_NOTIFICATIONS_ENDPOINT = "companies/disableNotifications";
  public static final String ENABLE_NOTIFICATIONS_ENDPOINT = "companies/enableNotifications";
  public static final URI PRODUCTION_BASE_URL = URI.create("https://api.invisiblecollector.com");

  private ApiRequestFacade apiFacade;
  private JsonModelFacade jsonFacade;
  private ValidatorBuilder validatorBuilder;

  public IcFacade(ApiRequestFacade apiFacade) {
    this(apiFacade, new JsonModelFacade());
  }

  private IcFacade(ApiRequestFacade apiFacade, JsonModelFacade jsonFacade) {
    this(apiFacade, jsonFacade, new ValidatorBuilder());
  }

  public IcFacade(ApiRequestFacade apiFacade, JsonModelFacade jsonFacade,
      ValidatorBuilder validatorBuilder) {
    this.apiFacade = apiFacade;
    this.jsonFacade = jsonFacade;
    this.validatorBuilder = validatorBuilder;

    this.validatorBuilder.addCommonValidators();
  }

  public IcFacade(String apiToken) {
    this(new ApiRequestFacade(apiToken, PRODUCTION_BASE_URL));
  }

  public IcFacade(String apiToken, URI baseUrl) {
    this(new ApiRequestFacade(apiToken, baseUrl));
  }

  public Customer registerNewCustomer(ICustomerUpdate costumerInfo)
      throws IcException, IcConflictingException {
    String jsonToSend = this.jsonFacade.toJson(costumerInfo);
    ValidatorBuilder builder =
        this.validatorBuilder.clone().addBadClientJsonValidator().addConflictValidator();

    return this.returningRequest(Customer.class, builder,
        (validator) -> apiFacade.postRequest(validator, CUSTOMERS_ENDPOINT, jsonToSend));
  }

  public Company requestCompanyInfo() throws IcException {
    ValidatorBuilder builder = this.validatorBuilder.clone();
    return this.returningRequest(Company.class, builder,
        (validator) -> apiFacade.getRequest(validator, COMPANIES_ENDPOINT));
  }

  public Customer requestCustomerInfo(String customerId) throws IcException {
    assertCorrectId(customerId);
    String endpoint = CUSTOMERS_ENDPOINT + "/" + customerId;
    ValidatorBuilder builder = this.validatorBuilder.clone();

    return this.returningRequest(Customer.class, builder,
        (validator) -> apiFacade.getRequest(validator, endpoint));
  }


  public Company setCompanyNotifications(boolean enableNotifications) throws IcException {
    IThrowingBuilder<InputStream, IValidator> requestMethod = enableNotifications
        ? (validator) -> apiFacade.putRequest(validator, ENABLE_NOTIFICATIONS_ENDPOINT, null)
        : (validator) -> apiFacade.putRequest(validator, DISABLE_NOTIFICATIONS_ENDPOINT, null);

    ValidatorBuilder builder = this.validatorBuilder.clone();
    return this.returningRequest(Company.class, builder, requestMethod);
  }

  public Map<String, String> setCustomerAttributes(IRoutable idContainer,
      Map<String, String> attributes) throws IcException {
    String id = getAndAssertCorrectId(idContainer);
    return setCustomerAttributes(id, attributes);
  }

  public Map<String, String> setCustomerAttributes(String customerId,
      Map<String, String> attributes) throws IcException {
    assertCorrectId(customerId);
    String endpoint = String.join("/", CUSTOMERS_ENDPOINT, customerId, ATTRIBUTES_PATH);
    String jsonToSend = this.jsonFacade.toJson(attributes);
    IValidator validator = this.validatorBuilder.clone().addBadClientJsonValidator().build();
    InputStream inputStream = apiFacade.postRequest(validator, endpoint, jsonToSend);

    return this.jsonFacade.parseStringStreamAsStringMap(inputStream);
  }

  public Company updateCompanyInfo(ICompanyUpdate companyInfo) throws IcException {
    ValidatorBuilder builder = this.validatorBuilder.clone().addBadClientJsonValidator();
    String jsonToSend = this.jsonFacade.toJson(companyInfo);
    return this.returningRequest(Company.class, builder,
        (validator) -> apiFacade.putRequest(validator, COMPANIES_ENDPOINT, jsonToSend));
  }

  public Customer updateCustomerInfo(ICustomerUpdate customerInfo, String customerId)
      throws IcException {
    assertCorrectId(customerId);
    String endpoint = CUSTOMERS_ENDPOINT + "/" + customerId;
    String json = this.jsonFacade.toJson(customerInfo);
    ValidatorBuilder builder = this.validatorBuilder.clone();

    return this.returningRequest(Customer.class, builder,
        (validator) -> apiFacade.putRequest(validator, endpoint, json));
  }

  public Map<String, String> getCustomerAttributes(IRoutable idContainer) throws IcException {
    String id = getAndAssertCorrectId(idContainer);
    return getCustomerAttributes(id);
  }

  public Map<String, String> getCustomerAttributes(String customerId) throws IcException {
    assertCorrectId(customerId);
    String endpoint = String.join("/", CUSTOMERS_ENDPOINT, customerId, ATTRIBUTES_PATH);
    IValidator validator = this.validatorBuilder.clone().build();
    InputStream inputStream = apiFacade.getRequest(validator, endpoint);

    return this.jsonFacade.parseStringStreamAsStringMap(inputStream);
  }

  private void assertCorrectId(String id) {
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("Id cannot be null");
    }
  }

  private String getAndAssertCorrectId(IRoutable idContainer) {
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

  private <T> T returningRequest(Class<T> returnType, ValidatorBuilder validatorBuilder,
      IThrowingBuilder<InputStream, IValidator> requestMethod) throws IcException {
    IValidator validator = validatorBuilder.addServerJsonValidator().build();
    InputStream inputStream = requestMethod.build(validator);

    return this.jsonFacade.parseStringStream(inputStream, returnType);
  }
}
