package com.invisiblecollector;

import com.invisiblecollector.connection.ApiRequestFacade;
import com.invisiblecollector.connection.RequestType;
import com.invisiblecollector.connection.response.ResponseValidator;
import com.invisiblecollector.exceptions.IcConflictingException;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.Company;
import com.invisiblecollector.model.Customer;
import com.invisiblecollector.model.Debt;
import com.invisiblecollector.model.FindDebtsBuilder;
import com.invisiblecollector.model.serialization.JsonModelFacade;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * A Thread-Safe and Immutable Container for the various api Invisible Collector operations.
 *
 * <p>Used to split the API surface area along logical divisions.
 *
 * <p>Use this as the entry point into the Library.
 *
 * @author ros
 */
public class IcApiFacade {

  private static final URI PRODUCTION_BASE_URL = URI.create("https://api.invisiblecollector.com");
  private static final String DEBTS_ENDPOINT = "debts";
  private static final String ATTRIBUTES_PATH = "attributes";
  private static final String CUSTOMERS_ENDPOINT = "customers";
  private static final String DEBTS_PATH = "debts";
  private static final String COMPANIES_ENDPOINT = "companies";
  private static final String DISABLE_NOTIFICATIONS_ENDPOINT = "companies/disableNotifications";
  private static final String ENABLE_NOTIFICATIONS_ENDPOINT = "companies/enableNotifications";
  private static final String DEBTS_FIND_PATH = "debts/find";
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

  private ApiRequestFacade apiFacade;
  private JsonModelFacade jsonFacade;

  /**
   * Creates an object with the default hostname (https://api.invisiblecollector.com).
   *
   * @param apiToken the company's Api Token
   * @see #IcApiFacade(String, URI)
   */
  public IcApiFacade(String apiToken) {
    this(apiToken, PRODUCTION_BASE_URL);
  }

  /**
   * Creates an object with a custom hostname or base path.
   *
   * @param apiToken the company's Api Token
   * @param baseUrl the hostname, scheme and optionally base path for the connection
   */
  public IcApiFacade(String apiToken, URI baseUrl) {
    this.jsonFacade = new JsonModelFacade();
    ResponseValidator responseValidator = new ResponseValidator(this.jsonFacade);
    this.apiFacade = new ApiRequestFacade(apiToken, baseUrl, responseValidator);
  }

  /**
   * Creates an IcApiFacade with injection
   *
   * <p>Preferably use {@link #IcApiFacade(String)} or {@link #IcApiFacade(String, URI)}
   *
   * @param apiFacade the api facade
   * @param jsonFacade teh json facade
   */
  public IcApiFacade(ApiRequestFacade apiFacade, JsonModelFacade jsonFacade) {
    this.apiFacade = apiFacade;
    this.jsonFacade = jsonFacade;
  }

  private void assertCorrectId(String id) throws IllegalArgumentException {
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("Id cannot be empty");
    }
  }

  //  private <T> T returningRequest(
  //          ThrowingSupplier<InputStream, IcException> requestMethod, Class<T> returnType)
  //      throws IcException {
  //    InputStream inputStream = requestMethod.get();
  //    return this.jsonFacade.parseStringStream(inputStream, returnType);
  //  }

  /**
   * Request the company info from the database.
   *
   * @return up-to-date company info.
   * @throws IcException on any general exception
   */
  public Company requestCompanyInfo() throws IcException {
    return this.jsonFacade.parseStringStream(
        apiFacade.jsonToJsonRequest(RequestType.GET, COMPANIES_ENDPOINT, null), Company.class);
  }

  /**
   * Enable or disable notifications for the company's customer.
   *
   * @param enableNotifications true: enable notifications, false: disable notifications
   * @return up-to-date company info.
   * @throws IcException on any general exception
   */
  public Company setCompanyNotifications(boolean enableNotifications) throws IcException {
    InputStream inputStream =
        enableNotifications
            ? apiFacade.jsonToJsonRequest(RequestType.PUT, ENABLE_NOTIFICATIONS_ENDPOINT, null)
            : apiFacade.jsonToJsonRequest(RequestType.PUT, DISABLE_NOTIFICATIONS_ENDPOINT, null);

    return this.jsonFacade.parseStringStream(inputStream, Company.class);
  }

  /**
   * Update company info.
   *
   * <p>You should use {@link #requestCompanyInfo()} before using this method to request company
   * info since the name and vatNumber mandatory company fields are needed for validation and
   * consistency purposes.
   *
   * @param companyInfo the company info. name and vatNumber are <b>mandatory</b> attributes.
   * @return up-to-date company info
   * @throws IcException on any general exception
   */
  public Company updateCompanyInfo(Company companyInfo) throws IcException {
    companyInfo.assertContainsKeys("name", "vatNumber");
    Map<String, Object> company =
        companyInfo.getOnlyFields("name", "vatNumber", "address", "zipCode", "city");
    return this.jsonFacade.parseStringStream(
        apiFacade.jsonToJsonRequest(RequestType.PUT, COMPANIES_ENDPOINT, company), Company.class);
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
    customerInfo.assertContainsKeys("name", "vatNumber", "country");
    Map<String, Object> fields = customerInfo.getOnlyFields(CUSTOMER_FIELDS);

    return this.jsonFacade.parseStringStream(
        apiFacade.jsonToJsonRequest(RequestType.POST, CUSTOMERS_ENDPOINT, fields), Customer.class);
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
    InputStream inputStream = apiFacade.jsonToJsonRequest(RequestType.GET, endpoint, null);

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
    InputStream inputStream = apiFacade.jsonToJsonRequest(RequestType.GET, endpoint, null);

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

    return this.jsonFacade.parseStringStream(
        apiFacade.jsonToJsonRequest(RequestType.GET, endpoint, null), Customer.class);
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
    InputStream inputStream = apiFacade.jsonToJsonRequest(RequestType.POST, endpoint, attributes);

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
    customerInfo.assertContainsKeys("country");
    Map<String, Object> fields = customerInfo.getOnlyFields(CUSTOMER_FIELDS);

    return this.jsonFacade.parseStringStream(
        apiFacade.jsonToJsonRequest(RequestType.PUT, endpoint, fields), Customer.class);
  }

  /**
   * Register a new debt related to a customer.
   *
   * @param debtInfo the debt information to register. The {@code number}, {@code customerId},
   *     {@code type}, {@code date} and {@code dueDate} are <b>mandatory</b> attributes. If the
   *     model contains items they must contain the {@code name} attribute.
   * @return an up-to-date object with the debt information.
   * @throws IcException any general exception
   * @see #registerNewDebt(Debt)
   */
  public Debt registerNewDebt(Debt debtInfo) throws IcException {
    debtInfo.assertContainsKeys("number", "customerId", "type", "date", "dueDate");
    Map<String, Object> fields =
        debtInfo.getOnlyFields(
            "number",
            "customerId",
            "type",
            "status",
            "date",
            "dueDate",
            "netTotal",
            "tax",
            "grossTotal",
            "currency",
            "items",
            "attributes");
    debtInfo.getItems().stream().forEach(item -> item.assertContainsKeys("name"));

    return this.jsonFacade.parseStringStream(
        apiFacade.jsonToJsonRequest(RequestType.POST, DEBTS_ENDPOINT, fields), Debt.class);
  }

  /**
   * Request debt information from the database.
   *
   * @param debtId the id of the debt whose information is requested.
   * @return up-to-date debt information.
   * @throws IcException any general exception
   */
  public Debt requestDebtInfo(String debtId) throws IcException {
    assertCorrectId(debtId);

    String endpoint = DEBTS_ENDPOINT + "/" + debtId;
    return this.jsonFacade.parseStringStream(
        apiFacade.jsonToJsonRequest(RequestType.GET, endpoint, null), Debt.class);
  }

  /**
   * Search the database for the debts that match the query
   *
   * @param findDebts the search query
   * @return found debts that match the query
   * @throws IcException on any general exception
   */
  public List<Debt> findDebts(FindDebtsBuilder findDebts) throws IcException {
    Map<String, Object> queryParams = findDebts.getFields();

    InputStream inputStream =
        this.apiFacade.uriEncodedToJsonRequest(RequestType.GET, DEBTS_FIND_PATH, queryParams);

    return this.jsonFacade.parseStringStreamAsDebtList(inputStream);
  }
}
