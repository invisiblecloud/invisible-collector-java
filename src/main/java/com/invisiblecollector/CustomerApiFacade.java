package com.invisiblecollector;

import com.invisiblecollector.connection.ApiRequestFacade;
import com.invisiblecollector.exceptions.IcConflictingException;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.Customer;
import com.invisiblecollector.model.Debt;

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

  private static final String ATTRIBUTES_PATH = "attributes";
  private static final String CUSTOMERS_ENDPOINT = "customers";
  private static final String DEBTS_PATH = "debts";
  private static final String[] CUSTOMER_FIELDS =
      new String[] {
        "name",
        "externalId",
        "vatNumber",
        "address",
        "zipCode",
        "city",
        "country",
        "email",
        "phone",
        "locale"
      };

  public CustomerApiFacade(ApiRequestFacade apiFacade) {
    super(apiFacade);
  }

  public CustomerApiFacade(String apiToken, URI baseUrl) {
    super(apiToken, baseUrl);
  }

  /**
   * Register a new customer in the database.
   *
   * @param customerInfo the object containing the customer information. Attributes {@code name},
   *     {@code country} and {@code vatNumber} are <b>mandatory</b>
   * @return an up to date {@link Customer} containing the customer's information
   * @throws IcException any general exception
   * @throws IcConflictingException in case the vatNumber or externalId already exists for another
   *     customer in the database. In that case this object contains the id of the conflicting
   *     customer.
   * @see #registerNewCustomer(Customer)
   */
  public Customer registerNewCustomer(Customer customerInfo)
      throws IcException, IcConflictingException {
    customerInfo.assertConstainsKeys("name", "vatNumber", "country");
    Map<String, Object> fields = customerInfo.getOnlyFields(CUSTOMER_FIELDS);
    String jsonToSend = this.jsonFacade.toJson(fields);

    return this.returningRequest(
        Customer.class, () -> apiFacade.postRequest(CUSTOMERS_ENDPOINT, jsonToSend));
  }

  /**
   * Get the customer attributes string map.
   *
   * <p>Use {@link #setCustomerAttributes(String, Map)} to set the attributes returned by this
   * method.
   *
   * @param customerId the id of the customer (can be the id or externalId).
   * @return a map containing up-to-date string:string attribute pairs which correspond to the
   *     customer.
   * @throws IcException in case of any error
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
   * @param customerId the id or external id of the customer
   * @return the customer's up-to-date list of debts
   * @throws IcException on any general error
   */
  public List<Debt> requestCustomerDebts(String customerId) throws IcException {
    assertCorrectId(customerId);
    String endpoint = String.join("/", CUSTOMERS_ENDPOINT, customerId, DEBTS_PATH);
    InputStream inputStream = apiFacade.getRequest(endpoint);

    return this.jsonFacade.parseStringStreamAsDebtList(inputStream);
  }

  /**
   * Get customer info from the database.
   *
   * @param customerId the id or externalId of the customer.
   * @return the up-to-date customer info
   * @throws IcException any general error
   */
  public Customer requestCustomerInfo(String customerId) throws IcException {
    assertCorrectId(customerId);
    String endpoint = CUSTOMERS_ENDPOINT + "/" + customerId;

    return this.returningRequest(Customer.class, () -> apiFacade.getRequest(endpoint));
  }

  /**
   * Set the customer's string map attributes.
   *
   * <p>Use {@link #requestCustomerAttributes(String)}
   *
   * @param customerId the id or externalId of the customer.
   * @param attributes the map with the attributes to set. Duplicate values in the database are
   *     updated, while pre-existing values in the database are unaffected.
   * @return a map containing up-to-date string:string attribute pairs which correspond to the
   *     customer.
   * @throws IcException any general error
   */
  public Map<String, String> setCustomerAttributes(
      String customerId, Map<String, String> attributes) throws IcException {
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
   * @param customerInfo the customer info. Must contain as well an id or externalId that indicates
   *     the customer to update. null values will be discarded
   */

  /**
   * Update the customer's info in the database.
   *
   * @param customerInfo the customer info. Must contain as well an id or externalId that indicates
   *     the customer to update. The {@code country} attribute is <b>mandatory</b>.
   * @return the up-to-date customer info.
   * @throws IcException any general exception
   * @see #updateCustomerInfo(Customer)
   */
  public Customer updateCustomerInfo(Customer customerInfo) throws IcException {
    String customerId = customerInfo.getRoutableId();
    String endpoint = CUSTOMERS_ENDPOINT + "/" + customerId;
    customerInfo.assertConstainsKeys("country");
    Map<String, Object> fields = customerInfo.getOnlyFields(CUSTOMER_FIELDS);
    String json = this.jsonFacade.toJson(fields);

    return this.returningRequest(Customer.class, () -> apiFacade.putRequest(endpoint, json));
  }
}
