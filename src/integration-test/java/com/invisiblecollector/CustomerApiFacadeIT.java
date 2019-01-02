package com.invisiblecollector;

import com.invisiblecollector.connection.RequestType;
import com.invisiblecollector.exceptions.IcConflictingException;
import com.invisiblecollector.model.Customer;
import com.invisiblecollector.model.Debt;
import com.invisiblecollector.model.builder.CustomerBuilder;
import com.invisiblecollector.model.builder.DebtBuilder;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class CustomerApiFacadeIT extends IcFacadeTestBase {

  // same as TEST_MAP in json form
  private static final String TEST_MAP_JSON = "{ \"a\":\"12\", \"b\":\"string\" }";
  private static final Map<String, String> TEST_MAP;
  private static final String ATTRIBUTES_PATH = "attributes";
  private static final String CUSTOMERS_ENDPOINT = "customers";
  private static final String DEBTS_PATH = "debts";

  static {
    Map<String, String> map = new TreeMap<>();
    map.put("a", "12");
    map.put("b", "string");
    TEST_MAP = Collections.unmodifiableMap(new HashMap<>(map));
  }

  private CustomerApiFacade buildCustomerResponseAndAddMockReponse(CustomerBuilder customerBuilder)
      throws Exception {
    return buildIcApiResponseAndAddServerReply(customerBuilder).getCustomerFacade();
  }

  private String assertCustomerAttributesGuts(
      RequestType requestType,
      IThrowingBuilder2<Map<String, String>, CustomerApiFacade, String> method)
      throws Exception {
    MockResponse response = buildBodiedMockResponse(TEST_MAP_JSON);
    CustomerApiFacade facade = initMockServer(response).getCustomerFacade();
    String id = "123";
    String endpoint = joinUriPaths(CUSTOMERS_ENDPOINT, id, ATTRIBUTES_PATH);
    Map<String, String> returnedMap = method.build(facade, id);
    Assertions.assertEquals(TEST_MAP, returnedMap);
    return endpoint;
  }

  @Test
  public void registerNewCustomer_success() throws Exception {
    CustomerBuilder customerBuilder = CustomerBuilder.buildTestCustomerBuilder();
    CustomerApiFacade icFacade = buildCustomerResponseAndAddMockReponse(customerBuilder);

    this.assertCorrectModelReturned(
        customerBuilder, (Customer customer) -> icFacade.registerNewCustomer(customer));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(
        request, CUSTOMERS_ENDPOINT, this.mockServer.getBaseUri(), RequestType.POST);
    assertSentCorrectJson(request, customerBuilder.buildSendableJson(true));
  }

  @Test
  public void registerNewCustomer_failMissingMandatoryField() throws Exception {
    CustomerBuilder customerBuilder = new CustomerBuilder().setGid("1232132");
    CustomerApiFacade icFacade = buildCustomerResponseAndAddMockReponse(customerBuilder);

    Customer correctCustomer = customerBuilder.buildModel(true);
    IllegalArgumentException ex =
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> icFacade.registerNewCustomer(correctCustomer));
    MatcherAssert.assertThat(ex.getMessage(), CoreMatchers.containsString("Field name is missing"));
  }

  @Test
  public void registerNewCustomer_onlyMandatoryFields() throws Exception {
    CustomerBuilder customerBuilder =
        new CustomerBuilder()
            .setGid("shouldn't appear")
            .setName("A Name")
            .setVatNumber("1234")
            .setCountry("PT")
            .setCity(null);
    CustomerApiFacade icFacade = buildCustomerResponseAndAddMockReponse(customerBuilder);
    Customer customer = customerBuilder.buildModel(true);
    icFacade.registerNewCustomer(customer);
    RecordedRequest request = this.mockServer.getRequest();
    assertSentCorrectJson(request, customerBuilder.buildSendableJson(true));
  }

  @Test
  public void registerNewCustomer_conflict() throws Exception {
    int statusCode = 409;
    String json = buildConflictErrorJson(statusCode);
    MockResponse mockResponse = buildBodiedMockResponse(json).setResponseCode(statusCode);
    CustomerApiFacade icFacade = initMockServer(mockResponse).getCustomerFacade();
    Customer correctCustomer = CustomerBuilder.buildTestCustomerBuilder().buildModel();
    IcConflictingException exception =
        Assertions.assertThrows(
            IcConflictingException.class, () -> icFacade.registerNewCustomer(correctCustomer));
    Assertions.assertEquals(CONFLICT_GID, exception.getGid());
    MatcherAssert.assertThat(
        exception.getMessage(), CoreMatchers.containsString(JSON_ERROR_MESSAGE));
  }

  @Test
  public void updateCustomerInfo_success() throws Exception {
    CustomerBuilder customerBuilder =
        CustomerBuilder.buildTestCustomerBuilder()
            .setName("Brand new new Name");
    String id = customerBuilder.getExternalId();
    String endpoint = joinUriPaths(CUSTOMERS_ENDPOINT, id);
    CustomerApiFacade icFacade = buildCustomerResponseAndAddMockReponse(customerBuilder);

    Customer sentCustomer =
            customerBuilder.buildModel();
    sentCustomer.setVatNumber(null);
    sentCustomer.setExternalId(null);

    this.assertCorrectModelReturned(
        customerBuilder,
        (unused) -> icFacade.updateCustomerInfo(sentCustomer));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, endpoint, this.mockServer.getBaseUri(), RequestType.PUT);

    customerBuilder.setVatNumber(null);
    customerBuilder.setExternalId(null);

    assertSentCorrectJson(request, customerBuilder.buildSendableJson(false));
  }

  @Test
  public void updateCustomerInfo_failBadId() throws Exception {
    CustomerBuilder customerBuilder =
        CustomerBuilder.buildTestCustomerBuilder()
            .setName("Brand new new Name")
            .setGid("")
            .setExternalId("");
    CustomerApiFacade icFacade = buildCustomerResponseAndAddMockReponse(customerBuilder);
    Customer correctCustomer = customerBuilder.buildModel();

    IllegalArgumentException ex =
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> icFacade.updateCustomerInfo(correctCustomer));
    MatcherAssert.assertThat(
        ex.getMessage(), CoreMatchers.containsString("no valid id contained in object"));
  }

  @Test
  public void requestCustomerInfo_success() throws Exception {
    CustomerBuilder customerBuilder = CustomerBuilder.buildTestCustomerBuilder();
    CustomerApiFacade icFacade = buildCustomerResponseAndAddMockReponse(customerBuilder);
    String id = customerBuilder.getExternalId();
    String endpoint = joinUriPaths(CUSTOMERS_ENDPOINT, id);
    this.assertCorrectModelReturned(
        customerBuilder, (customer) -> icFacade.requestCustomerInfo(id));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, endpoint, this.mockServer.getBaseUri(), RequestType.GET);
  }

  @Test
  public void setCustomerAttributes_success() throws Exception {
    String endpoint =
        assertCustomerAttributesGuts(
            RequestType.POST, (facade, id) -> facade.setCustomerAttributes(id, TEST_MAP));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(
        request, endpoint, this.mockServer.getBaseUri(), RequestType.POST);
    assertSentCorrectJson(request, TEST_MAP_JSON);
  }

  @Test
  public void getCustomerAttributes_success() throws Exception {
    String endpoint =
        assertCustomerAttributesGuts(
            RequestType.GET, (facade, id) -> facade.requestCustomerAttributes(id));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, endpoint, this.mockServer.getBaseUri(), RequestType.GET);
  }

  private static final String TEST_ID = "1234";

  @Test
  public void requestCustomerDebts_successMultipleDebts() throws Exception {

    DebtBuilder builder1 = DebtBuilder.buildMinimalTestBuilder();
    DebtBuilder builder2 = DebtBuilder.buildTestDebtBuilder();

    String json = "[" + builder1.buildJson() + "," + builder2.buildJson() + "]";
    MockResponse mockResponse = buildBodiedMockResponse(json);
    CustomerApiFacade customerFacade = initMockServer(mockResponse).getCustomerFacade();

    List<Debt> returnedDebts = customerFacade.requestCustomerDebts(TEST_ID);
    List<Debt> debts = new ArrayList<>();
    debts.add(builder1.buildModel());
    debts.add(builder2.buildModel());

    assertObjectsEquals(debts, returnedDebts);
    RecordedRequest request = this.mockServer.getRequest();
    String endpoint = joinUriPaths(CUSTOMERS_ENDPOINT, TEST_ID, DEBTS_PATH);
    this.assertSentCorrectHeaders(request, endpoint, this.mockServer.getBaseUri(), RequestType.GET);
  }

  @Test
  public void requestCustomerDebts_successEmptyList() throws Exception {

    String json = "[ ]";
    MockResponse mockResponse = buildBodiedMockResponse(json);
    CustomerApiFacade customerFacade = initMockServer(mockResponse).getCustomerFacade();

    List<Debt> returnedDebts = customerFacade.requestCustomerDebts(TEST_ID);
    List<Debt> debts = new ArrayList<>();
    assertObjectsEquals(debts, returnedDebts);
    RecordedRequest request = this.mockServer.getRequest();
    String endpoint = joinUriPaths(CUSTOMERS_ENDPOINT, TEST_ID, DEBTS_PATH);
    this.assertSentCorrectHeaders(request, endpoint, this.mockServer.getBaseUri(), RequestType.GET);
  }
}
