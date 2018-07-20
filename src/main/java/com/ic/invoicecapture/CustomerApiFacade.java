package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import com.ic.invoicecapture.connection.response.validators.IValidator;
import com.ic.invoicecapture.connection.response.validators.ValidatorBuilder;
import com.ic.invoicecapture.exceptions.IcConflictingException;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Customer;
import com.ic.invoicecapture.model.CustomerField;
import com.ic.invoicecapture.model.IRoutable;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

/**
 * Immutable and thread safe class.
 * 
 * @author ros
 *
 */
public class CustomerApiFacade extends ApiBase {

  public static final String ATTRIBUTES_PATH = "attributes";
  public static final String CUSTOMERS_ENDPOINT = "customers";

  public CustomerApiFacade(ApiRequestFacade apiFacade) {
    super(apiFacade);
  }

  public CustomerApiFacade(String apiToken, URI baseUrl) {
    super(apiToken, baseUrl);
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

  public Customer registerNewCustomer(Customer customerInfo)
      throws IcException, IcConflictingException {
    return this.registerNewCustomer(customerInfo.toEnumMap());
  }

  public Customer registerNewCustomer(Map<CustomerField, Object> customerInfo)
      throws IcException, IcConflictingException {
    CustomerField.assertCorrectlyInitialized(customerInfo);
    String jsonToSend = this.jsonFacade.toJson(customerInfo);
    ValidatorBuilder builder =
        this.validatorBuilder.clone().addBadClientJsonValidator().addConflictValidator();

    return this.returningRequest(Customer.class, builder,
        (validator) -> apiFacade.postRequest(validator, CUSTOMERS_ENDPOINT, jsonToSend));
  }

  public Customer requestCustomerInfo(String customerId)
      throws IcException, IcConflictingException {
    assertCorrectId(customerId);
    String endpoint = CUSTOMERS_ENDPOINT + "/" + customerId;
    ValidatorBuilder builder = this.validatorBuilder.clone();

    return this.returningRequest(Customer.class, builder,
        (validator) -> apiFacade.getRequest(validator, endpoint));
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

  public Customer updateCustomerInfo(Customer customerInfo) throws IcException {
    String id = getAndAssertCorrectId(customerInfo);
    return this.updateCustomerInfo(customerInfo.toEnumMap(), id);
  }

  public Customer updateCustomerInfo(Map<CustomerField, Object> customerInfo, String customerId)
      throws IcException {
    assertCorrectId(customerId);
    String endpoint = CUSTOMERS_ENDPOINT + "/" + customerId;
    String json = this.jsonFacade.toJson(customerInfo);
    ValidatorBuilder builder = this.validatorBuilder.clone().addBadClientJsonValidator();

    return this.returningRequest(Customer.class, builder,
        (validator) -> apiFacade.putRequest(validator, endpoint, json));
  }

}
