package com.invisiblecollector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.invisiblecollector.connection.RequestType;
import com.invisiblecollector.exceptions.IcConflictingException;
import com.invisiblecollector.model.Customer;
import com.invisiblecollector.model.Debt;
import com.invisiblecollector.model.builder.CustomerBuilder;
import com.invisiblecollector.model.builder.DebtBuilder;
import com.invisiblecollector.model.json.JsonTestUtils;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

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

  private String assertCustomerAttributesGuts(RequestType requestType,
      IThrowingBuilder2<Map<String, String>, CustomerApiFacade, String> method) throws Exception {
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

    this.assertCorrectModelReturned(customerBuilder,
        (Customer customer) -> icFacade.registerNewCustomer(customer));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, CUSTOMERS_ENDPOINT, this.mockServer.getBaseUri(),
        RequestType.POST);
    assertSentCorrectJson(request, customerBuilder.buildSendableJson());
  }

  @Test
  public void registerNewCustomer_failMissingMandatoryField() throws Exception {
    CustomerBuilder customerBuilder = new CustomerBuilder().setGid("1232132");
    CustomerApiFacade icFacade = buildCustomerResponseAndAddMockReponse(customerBuilder);

    Customer correctCustomer = customerBuilder.buildModel();
    IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class,
            () -> icFacade.registerNewCustomer(correctCustomer));
    MatcherAssert.assertThat(ex.getMessage(), CoreMatchers.containsString("of Model 'Customer' MUST be present"));
  }

  @Test
  public void registerNewCustomer_onlyMandatoryFields() throws Exception {
    CustomerBuilder customerBuilder = new CustomerBuilder().setGid("shouldn't appear")
        .setName("A Name").setVatNumber("1234").setCity(null);
    CustomerApiFacade icFacade = buildCustomerResponseAndAddMockReponse(customerBuilder);
    Customer customer = customerBuilder.buildModel();
    icFacade.registerNewCustomer(customer);
    RecordedRequest request = this.mockServer.getRequest();
    assertSentCorrectJson(request, customerBuilder.buildSendableJson());
  }

  @Test
  public void registerNewCustomer_conflict() throws Exception {
    int statusCode = 409;
    String json = buildConflictErrorJson(statusCode);
    MockResponse mockResponse = buildBodiedMockResponse(json).setResponseCode(statusCode);
    CustomerApiFacade icFacade = initMockServer(mockResponse).getCustomerFacade();
    Customer correctCustomer = CustomerBuilder.buildTestCustomerBuilder().buildModel();
    IcConflictingException exception = Assertions.assertThrows(IcConflictingException.class,
        () -> icFacade.registerNewCustomer(correctCustomer));
    Assertions.assertEquals(CONFLICT_GID, exception.getGid());
  }

  @Test
  public void updateCustomerInfo_success() throws Exception {
    CustomerBuilder customerBuilder =
        CustomerBuilder.buildTestCustomerBuilder().setName("Brand new new Name");
    String id = customerBuilder.getExternalId();
    String endpoint = joinUriPaths(CUSTOMERS_ENDPOINT, id);
    CustomerApiFacade icFacade = buildCustomerResponseAndAddMockReponse(customerBuilder);

    this.assertCorrectModelReturned(customerBuilder, (unused) -> {
      Customer updateCustomer = customerBuilder.setVatNumber(null).setExternalId(null).buildModel();
      return icFacade.updateCustomerInfo(updateCustomer.toEnumMap(), id);
    });
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, endpoint, this.mockServer.getBaseUri(), RequestType.PUT);
    assertSentCorrectJson(request, customerBuilder.buildSendableJson());
  }

  @Test
  public void updateCustomerInfo_failBadId() throws Exception {
    CustomerBuilder customerBuilder =
        CustomerBuilder.buildTestCustomerBuilder().setName("Brand new new Name");
    String id = "";
    CustomerApiFacade icFacade = buildCustomerResponseAndAddMockReponse(customerBuilder);
    Customer correctCustomer = customerBuilder.buildModel();

    IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class,
            () -> icFacade.updateCustomerInfo(correctCustomer.toEnumMap(), id));
    MatcherAssert.assertThat(ex.getMessage(), CoreMatchers.containsString("Id cannot be empty"));
  }

  @Test
  public void requestCustomerInfo_success() throws Exception {
    CustomerBuilder customerBuilder = CustomerBuilder.buildTestCustomerBuilder();
    CustomerApiFacade icFacade = buildCustomerResponseAndAddMockReponse(customerBuilder);
    String id = customerBuilder.getExternalId();
    String endpoint = joinUriPaths(CUSTOMERS_ENDPOINT, id);
    this.assertCorrectModelReturned(customerBuilder,
        (customer) -> icFacade.requestCustomerInfo(id));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, endpoint, this.mockServer.getBaseUri(), RequestType.GET);
  }

  @Test
  public void setCustomerAttributes_success() throws Exception {
    String endpoint = assertCustomerAttributesGuts(RequestType.POST,
        (facade, id) -> facade.setCustomerAttributes(id, TEST_MAP));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, endpoint, this.mockServer.getBaseUri(),
        RequestType.POST);
    assertSentCorrectJson(request, TEST_MAP_JSON);
  }

  @Test
  public void getCustomerAttributes_success() throws Exception {
    String endpoint = assertCustomerAttributesGuts(RequestType.GET,
        (facade, id) -> facade.requestCustomerAttributes(id));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, endpoint, this.mockServer.getBaseUri(), RequestType.GET);
  }

  private static final String TEST_ID = "1234";

  private interface DebtList extends List<Debt> {};

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
    JsonTestUtils.assertObjectsEqualsAsJson(debts, returnedDebts);
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
    JsonTestUtils.assertObjectsEqualsAsJson(debts, returnedDebts);
    RecordedRequest request = this.mockServer.getRequest();
    String endpoint = joinUriPaths(CUSTOMERS_ENDPOINT, TEST_ID, DEBTS_PATH);
    this.assertSentCorrectHeaders(request, endpoint, this.mockServer.getBaseUri(), RequestType.GET);
  }


}
