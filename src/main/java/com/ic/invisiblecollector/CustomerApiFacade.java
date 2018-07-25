package com.ic.invisiblecollector;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import com.ic.invisiblecollector.connection.ApiRequestFacade;
import com.ic.invisiblecollector.connection.response.validators.IValidator;
import com.ic.invisiblecollector.connection.response.validators.ValidatorBuilder;
import com.ic.invisiblecollector.exceptions.IcConflictingException;
import com.ic.invisiblecollector.exceptions.IcException;
import com.ic.invisiblecollector.model.Customer;
import com.ic.invisiblecollector.model.CustomerField;
import com.ic.invisiblecollector.model.IInternallyRoutable;

/**
 * Immutable and thread safe class for making operations on the {@code /customers } endpoint.
 * 
 * <p>For object construction see {@link IcApiFacade}
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

  /**
   * Get the customer attributes string map.
   * 
   * <p>Wrapper for a {@code GET /customers/:id/attributes } request.
   * 
   * <p>See also {@link #getCustomerAttributes(String)}.
   * 
   * <p>Use {@link #setCustomerAttributes(IInternallyRoutable, Map)} 
   * or {@link #setCustomerAttributes(String, Map)} 
   * for a way to the attributes returned by this method.
   * 
   * @param idContainer An object (such as an {@link Customer} object) that constains the id or
   *        externalId of the customer
   * @return a map containing up-to-date string:string attribute 
   *         pairs which correspond to the customer.
   * @throws IcException in case of any error
   */
  public Map<String, String> getCustomerAttributes(IInternallyRoutable idContainer)
      throws IcException {
    String id = getAndAssertCorrectId(idContainer);
    return getCustomerAttributes(id);
  }

  /**
   * Same as {@link #getCustomerAttributes(IInternallyRoutable)} but with the id pre-extracted.
   * 
   * @param customerId the id of the customer (can be the id or externalId).
   * @return same as {@link #registerNewCustomer(Customer)}
   * @throws IcException same as {@link #registerNewCustomer(Customer)}
   */
  public Map<String, String> getCustomerAttributes(String customerId) throws IcException {
    assertCorrectId(customerId);
    String endpoint = String.join("/", CUSTOMERS_ENDPOINT, customerId, ATTRIBUTES_PATH);
    IValidator validator = this.validatorBuilder.clone().build();
    InputStream inputStream = apiFacade.getRequest(validator, endpoint);

    return this.jsonFacade.parseStringStreamAsStringMap(inputStream);
  }

  /**
   * Register a new customer in the database. 
   * 
   * <p>Wrapper for a {@code POST /customers} request.
   * 
   * <p>See also {@link #registerNewCustomer(Map)}
   * 
   * @param customerInfo the object containing the customer information. 
   *        null values will be discarded
   * @return an up to date object containing the customer's information
   * @throws IcException any general exception
   * @throws IcConflictingException in case the vatNumber or externalId already exists 
   *         for another customer in the database. In that case this object contains the 
   *         id of the conflicting customer.
   */
  public Customer registerNewCustomer(Customer customerInfo)
      throws IcException, IcConflictingException {
    return this.registerNewCustomer(customerInfo.toEnumMap());
  }

  /**
   * Same as {@link #registerNewCustomer(Customer)} but with the abillity to send null fields.
   * 
   * @param customerInfo a map containing the fields of the customer to be created. 
   *        See {@link CustomerField} for a description of the fields and their possible values.
   * @return same as {@link #registerNewCustomer(Customer)}
   * @throws IcException same as {@link #registerNewCustomer(Customer)}
   * @throws IcConflictingException same as {@link #registerNewCustomer(Customer)}
   */
  public Customer registerNewCustomer(Map<CustomerField, Object> customerInfo)
      throws IcException, IcConflictingException {
    CustomerField.assertCorrectlyInitialized(customerInfo);
    String jsonToSend = this.jsonFacade.toJson(customerInfo);
    ValidatorBuilder builder = this.validatorBuilder.clone().addBadClientJsonValidator()
        .addConflictValidator("Entity already exists with the same VAT number or externalId");

    return this.returningRequest(Customer.class, builder,
        (validator) -> apiFacade.postRequest(validator, CUSTOMERS_ENDPOINT, jsonToSend));
  }

  /**
   * Get customer info from the database.
   * 
   * <p>See also {@link #requestCustomerInfo(String)}
   * 
   * @param idContainer an object containing the id or externalId of 
   *        the customer (can be a {@link Customer} object).
   * @return the up-to-date customer info
   * @throws IcException any general error
   */
  public Customer requestCustomerInfo(IInternallyRoutable idContainer)
      throws IcException {
    String id = getAndAssertCorrectId(idContainer);
    return requestCustomerInfo(id);
  }
  
  /**
   * Same as {@link #requestCustomerInfo(IInternallyRoutable)} but with the id pre-extracted.
   * 
   * @param customerId the id or externalId of the customer.
   * @return same as {@link #requestCustomerInfo(IInternallyRoutable)}
   * @throws IcException same as {@link #requestCustomerInfo(IInternallyRoutable)}
   */
  public Customer requestCustomerInfo(String customerId)
      throws IcException {
    assertCorrectId(customerId);
    String endpoint = CUSTOMERS_ENDPOINT + "/" + customerId;
    ValidatorBuilder builder = this.validatorBuilder.clone();

    return this.returningRequest(Customer.class, builder,
        (validator) -> apiFacade.getRequest(validator, endpoint));
  }

  /**
   * Set the customer's string map attributes.
   * 
   * <p>Same as {@link #setCustomerAttributes(String, Map)}.
   * 
   * <p>Use {@link #getCustomerAttributes(String)} or 
   * {@link #getCustomerAttributes(IInternallyRoutable)} to get the attributes that are set.
   * 
   * @param idContainer idContainer an object containing the id or externalId of 
   *        the customer (can be a {@link Customer} object).
   * @param attributes the map with the attributes to set. 
   *        Duplicate values in the database are updated, 
   *        while pre-existing values in the database are unaffected.
   * @return a map containing up-to-date string:string attribute 
   *         pairs which correspond to the customer.
   * @throws IcException any general error
   */
  public Map<String, String> setCustomerAttributes(IInternallyRoutable idContainer,
      Map<String, String> attributes) throws IcException {
    String id = getAndAssertCorrectId(idContainer);
    return setCustomerAttributes(id, attributes);
  }

  /**
   * Same as {@link #setCustomerAttributes(IInternallyRoutable, Map)} but with the
   * id or externalId pre-extracted.
   * 
   * @param customerId the id or externalId of the customer.
   * @param attributes same as {@link #setCustomerAttributes(IInternallyRoutable, Map)}
   * @return same as {@link #setCustomerAttributes(IInternallyRoutable, Map)}
   * @throws IcException same as {@link #setCustomerAttributes(IInternallyRoutable, Map)}
   */
  public Map<String, String> setCustomerAttributes(String customerId,
      Map<String, String> attributes) throws IcException {
    assertCorrectId(customerId);
    String endpoint = String.join("/", CUSTOMERS_ENDPOINT, customerId, ATTRIBUTES_PATH);
    String jsonToSend = this.jsonFacade.toJson(attributes);
    IValidator validator = this.validatorBuilder.clone().addBadClientJsonValidator().build();
    InputStream inputStream = apiFacade.postRequest(validator, endpoint, jsonToSend);

    return this.jsonFacade.parseStringStreamAsStringMap(inputStream);
  }

  /**
   * Updates the customer's info in the database.
   * 
   * <p>See also {@link #updateCustomerInfo(Map, String)}
   * 
   * @param customerInfo the customer info. Must contain as well 
   *        an id or externalId that indicates the customer to update. null values will be discarded
   * @return the up-to-date customer info.
   * @throws IcException any general exception
   */
  public Customer updateCustomerInfo(Customer customerInfo) throws IcException {
    String id = getAndAssertCorrectId(customerInfo);
    return this.updateCustomerInfo(customerInfo.toEnumMap(), id);
  }

  /**
   * Same as {@link #updateCustomerInfo(Customer)} but with the customer id pre-extracted.
   * 
   * @param customerInfo the new customer info. null values will be sent. 
   *        See {@link CustomerField} for a list and description of the fields of a customer.
   * @param customerId the id or externalId of the customer.
   * @return same as {@link #updateCustomerInfo(Map, String)}
   * @throws IcException same as {@link #updateCustomerInfo(Map, String)}
   */
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
