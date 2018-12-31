package com.invisiblecollector;

import com.invisiblecollector.connection.ApiRequestFacade;
import com.invisiblecollector.exceptions.IcConflictingException;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.Customer;
import com.invisiblecollector.model.CustomerField;
import com.invisiblecollector.model.Debt;
import com.invisiblecollector.model.IRoutable;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Immutable and thread safe class for making operations on the {@code /customers } API endpoint.
 * 
 * <p>For object construction see {@link IcApiFacade}
 * 
 * @author ros
 */
public class CustomerApiFacade extends ApiBase {

  private interface DebtList extends List<Debt> {} //used to get type of List<Debt>
  private static final String ATTRIBUTES_PATH = "attributes";
  private static final String CUSTOMERS_ENDPOINT = "customers";

  private static final String DEBTS_PATH = "debts";
  
  public CustomerApiFacade(ApiRequestFacade apiFacade) {
    super(apiFacade);
  }

  public CustomerApiFacade(String apiToken, URI baseUrl) {
    super(apiToken, baseUrl);
  }

  /**
   * Register a new customer in the database. 
   * 
   * <p>See {@link #registerNewCustomer(Map)} for more details.
   * 
   * @param customerInfo the object containing the customer information. 
   *        null values will be discarded
   */
  public Customer registerNewCustomer(Customer customerInfo)
      throws IcException, IcConflictingException {
    return this.registerNewCustomer(customerInfo.toEnumMap());
  }

  /**
   * Register a new customer in the database. 
   * 
   * @param customerInfo a map containing the fields of the customer to be created. 
   *        See {@link CustomerField} for a description of the attributes and their possible values.
   *        null values are <b>not</b> discarded.
   *        See {@link CustomerField#assertCorrectlyInitialized(Map)} for a list of the 
   *        <b>mandatory</b> attributes.
   * @return an up to date {@link Customer} containing the customer's information
   * @throws IcException any general exception
   * @throws IcConflictingException in case the vatNumber or externalId already exists 
   *         for another customer in the database. In that case this object contains the 
   *         id of the conflicting customer.
   * @see #registerNewCustomer(Customer)
   */
  public Customer registerNewCustomer(Map<CustomerField, Object> customerInfo)
      throws IcException, IcConflictingException {
    CustomerField.assertCorrectlyInitialized(customerInfo);
    String jsonToSend = this.jsonFacade.toJson(customerInfo);

    return this.returningRequest(Customer.class,
            () -> apiFacade.postRequest(CUSTOMERS_ENDPOINT, jsonToSend));
  }

  /**
   * Get the customer attributes string map.
   * 
   * <p>See {@link #requestCustomerAttributes(String)} for more details.
   * 
   * @param idContainer An object (such as a {@link Customer} object) that contains the id or
   *        externalId of the customer
   */
  public Map<String, String> requestCustomerAttributes(IRoutable idContainer)
      throws IcException {
    String id = idContainer.getRoutableId();
    return requestCustomerAttributes(id);
  }

  /**
   * Get the customer attributes string map.
   * 
   * <p>Use {@link #setCustomerAttributes(IRoutable, Map)} 
   * or {@link #setCustomerAttributes(String, Map)} 
   * to set the attributes returned by this method.
   * 
   * @param customerId the id of the customer (can be the id or externalId).
   * @return a map containing up-to-date string:string attribute 
   *         pairs which correspond to the customer.
   * @throws IcException in case of any error
   * @see #requestCustomerAttributes(IRoutable)
   */
  public Map<String, String> requestCustomerAttributes(String customerId) throws IcException {
    assertCorrectId(customerId);
    String endpoint = String.join("/", CUSTOMERS_ENDPOINT, customerId, ATTRIBUTES_PATH);
    InputStream inputStream = apiFacade.getRequest(endpoint);

    return this.jsonFacade.parseStringStreamAsStringMap(inputStream);
  }

  /**
   * Get the customer's debts.
   * 
   * <p>See {@link #requestCustomerDebts(String)} for more details.
   * 
   * @param idContainer an object containing the id or externalId of 
   *        the customer (can be a {@link Customer} object).
   */
  public List<Debt> requestCustomerDebts(IRoutable idContainer) throws IcException {
    String id = idContainer.getRoutableId();
    return requestCustomerDebts(id);
  }
  
  /**
   * Get the customer's debts.
   * 
   * @param customerId the id or external id of the customer
   * @return the customer's up-to-date list of debts
   * @throws IcException on any general error
   */
  public List<Debt> requestCustomerDebts(String customerId) throws IcException {
    assertCorrectId(customerId);
    String endpoint = String.join("/", CUSTOMERS_ENDPOINT, customerId, DEBTS_PATH);

    return this.returningRequest(DebtList.class,
            () -> apiFacade.getRequest(endpoint));
  }

  /**
   * Get customer info from the database.
   * 
   * <p>See {@link #requestCustomerInfo(String)} for more details.
   * 
   * @param idContainer an object containing the id or externalId of 
   *        the customer (can be a {@link Customer} object).
   */
  public Customer requestCustomerInfo(IRoutable idContainer)
      throws IcException {
    String id = idContainer.getRoutableId();
    return requestCustomerInfo(id);
  }

  /**
   * Get customer info from the database.
   * 
   * @param customerId the id or externalId of the customer.
   * @return the up-to-date customer info
   * @throws IcException any general error
   * @see #requestCustomerInfo(IRoutable)
   */
  public Customer requestCustomerInfo(String customerId)
      throws IcException {
    assertCorrectId(customerId);
    String endpoint = CUSTOMERS_ENDPOINT + "/" + customerId;

    return this.returningRequest(Customer.class,
            () -> apiFacade.getRequest(endpoint));
  }

  /**
   * Set the customer's string map attributes.
   * 
   * <p>See {@link #setCustomerAttributes(String, Map)} for more details.
   * 
   * @param idContainer an object containing the id or externalId of 
   *        the customer (can be a {@link Customer} object).
   */
  public Map<String, String> setCustomerAttributes(IRoutable idContainer,
      Map<String, String> attributes) throws IcException {
    String id = idContainer.getRoutableId();
    return setCustomerAttributes(id, attributes);
  }

  /**
   * Set the customer's string map attributes.
   * 
   * <p>Use {@link #requestCustomerAttributes(String)} or 
   * {@link #requestCustomerAttributes(IRoutable)} to get the attributes that are set.
   * 
   * @param customerId the id or externalId of the customer.
   * @param attributes the map with the attributes to set. 
   *        Duplicate values in the database are updated, 
   *        while pre-existing values in the database are unaffected.
   * @return a map containing up-to-date string:string attribute 
   *         pairs which correspond to the customer.
   * @throws IcException any general error
   * @see #setCustomerAttributes(IRoutable, Map)
   */
  public Map<String, String> setCustomerAttributes(String customerId,
      Map<String, String> attributes) throws IcException {
    assertCorrectId(customerId);
    String endpoint = String.join("/", CUSTOMERS_ENDPOINT, customerId, ATTRIBUTES_PATH);
    String jsonToSend = this.jsonFacade.toJson(attributes);
    InputStream inputStream = apiFacade.postRequest(endpoint, jsonToSend);

    return this.jsonFacade.parseStringStreamAsStringMap(inputStream);
  }

  /**
   * Update the customer's info in the database.
   * 
   * <p>See {@link #updateCustomerInfo(Map, String)} for more details.
   * 
   * @param customerInfo the customer info. Must contain as well 
   *        an id or externalId that indicates the customer to update. null values will be discarded
   */
  public Customer updateCustomerInfo(Customer customerInfo) throws IcException {
    String id = customerInfo.getRoutableId();
    return this.updateCustomerInfo(customerInfo.toEnumMap(), id);
  }
  
  /**
   * Update the customer's info in the database.
   * 
   * @param customerInfo the new customer info. null values will <b>not</b> be discarded. 
   *        See {@link CustomerField} for a list and description of the fields of a customer.
   *        See {@link CustomerField#assertCorrectlyInitialized(Map)} for a list of the 
   *        <b>mandatory</b> attributes.
   * @param customerId the id or externalId of the customer.
   * @return the up-to-date customer info.
   * @throws IcException any general exception
   * @see #updateCustomerInfo(Customer)
   */
  public Customer updateCustomerInfo(Map<CustomerField, Object> customerInfo, String customerId)
      throws IcException {
    assertCorrectId(customerId);
    String endpoint = CUSTOMERS_ENDPOINT + "/" + customerId;
    String json = this.jsonFacade.toJson(customerInfo);

    return this.returningRequest(Customer.class,
            () -> apiFacade.putRequest(endpoint, json));
  }
  
  
}
