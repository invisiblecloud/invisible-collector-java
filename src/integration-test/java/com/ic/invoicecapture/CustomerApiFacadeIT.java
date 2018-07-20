package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.RequestType;
import com.ic.invoicecapture.connection.builders.IThrowingBuilder;
import com.ic.invoicecapture.exceptions.IcConflictingException;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Customer;
import com.ic.invoicecapture.model.builder.CustomerBuilder;
import java.util.Map;
import java.util.TreeMap;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomerApiFacadeIT extends IcFacadeTestBase {

  // same as TEST_MAP in json form
  private static String TEST_MAP_JSON = "{ \"a\":\"12\", \"b\":\"string\" }";
  private static Map<String, String> TEST_MAP = new TreeMap<>();

  static {
    TEST_MAP.put("a", "12");
    TEST_MAP.put("b", "string");
  }

  public CustomerApiFacade buildCustomerResponse(CustomerBuilder customerBuilder) throws Exception {
    String json = customerBuilder.buildJson();
    MockResponse mockResponse = buildBodiedMockResponse(json);
    return initMockServer(mockResponse).getCustomerApiFacade();
  }

  public void assertCorrectCustomerReturned(CustomerBuilder customerBuilder,
      IThrowingBuilder<Customer, Customer> method) throws IcException {
    Customer correctCustomer = customerBuilder.buildModel();
    Customer returnedCustomer = method.build(correctCustomer);
    Assertions.assertEquals(correctCustomer, returnedCustomer);
  }

  @Test
  public void registerNewCustomer_success() throws Exception {
    CustomerBuilder customerBuilder = CustomerBuilder.buildTestCustomerBuilder();
    CustomerApiFacade icFacade = buildCustomerResponse(customerBuilder);

    this.assertCorrectCustomerReturned(customerBuilder,
        (customer) -> icFacade.registerNewCustomer(customer));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectBodiesHeaders(request, CustomerApiFacade.CUSTOMERS_ENDPOINT,
        this.mockServer.getBaseUri(), RequestType.POST);
    assertSentCorrectJson(request, customerBuilder.buildSendableJson());
  }

  @Test
  public void registerNewCustomer_failMissingMandatoryField() throws Exception {
    CustomerBuilder customerBuilder = new CustomerBuilder().setGid("1232132");
    CustomerApiFacade icFacade = buildCustomerResponse(customerBuilder);

    Customer correctCustomer = customerBuilder.buildModel();
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> icFacade.registerNewCustomer(correctCustomer));
  }

  @Test
  public void registerNewCustomer_onlyMandatoryFields() throws Exception {
    CustomerBuilder customerBuilder = new CustomerBuilder().setGid("shouldn't appear")
        .setName("A Name").setVatNumber("1234").setCity(null);
    CustomerApiFacade icFacade = buildCustomerResponse(customerBuilder);
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
    CustomerApiFacade icFacade = initMockServer(mockResponse).getCustomerApiFacade();
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
    String endpoint = joinUriPaths(CustomerApiFacade.CUSTOMERS_ENDPOINT, id);
    CustomerApiFacade icFacade = buildCustomerResponse(customerBuilder);

    this.assertCorrectCustomerReturned(customerBuilder, (unused) -> {
      Customer updateCustomer = customerBuilder.setVatNumber(null).setExternalId(null).buildModel();
      return icFacade.updateCustomerInfo(updateCustomer.toEnumMap(), id);
    });
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectBodiesHeaders(request, endpoint, this.mockServer.getBaseUri(),
        RequestType.PUT);
    assertSentCorrectJson(request, customerBuilder.buildSendableJson());
  }

  @Test
  public void updateCustomerInfo_failBadId() throws Exception {
    CustomerBuilder customerBuilder =
        CustomerBuilder.buildTestCustomerBuilder().setName("Brand new new Name");
    String id = "";
    CustomerApiFacade icFacade = buildCustomerResponse(customerBuilder);
    Customer correctCustomer = customerBuilder.buildModel();

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> icFacade.updateCustomerInfo(correctCustomer.toEnumMap(), id));
  }

  @Test
  public void requestCustomerInfo_success() throws Exception {
    CustomerBuilder customerBuilder = CustomerBuilder.buildTestCustomerBuilder();
    CustomerApiFacade icFacade = buildCustomerResponse(customerBuilder);
    String id = customerBuilder.getExternalId();
    String endpoint = joinUriPaths(CustomerApiFacade.CUSTOMERS_ENDPOINT, id);
    this.assertCorrectCustomerReturned(customerBuilder,
        (customer) -> icFacade.requestCustomerInfo(id));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectBodiesHeaders(request, endpoint, this.mockServer.getBaseUri(),
        RequestType.GET);
  }

  private String assertCustomerAttributesGuts(RequestType requestType,
      IThrowingBuilder2<Map<String, String>, CustomerApiFacade, String> method) throws Exception {
    MockResponse response = buildBodiedMockResponse(TEST_MAP_JSON);
    CustomerApiFacade facade = initMockServer(response).getCustomerApiFacade();
    String id = "123";
    String endpoint =
        joinUriPaths(CustomerApiFacade.CUSTOMERS_ENDPOINT, id, CustomerApiFacade.ATTRIBUTES_PATH);
    Map<String, String> returnedMap = method.build(facade, id);
    Assertions.assertEquals(TEST_MAP, returnedMap);
    return endpoint;
  }

  @Test
  public void setCustomerAttributes_success() throws Exception {
    String endpoint = assertCustomerAttributesGuts(RequestType.POST,
        (facade, id) -> facade.setCustomerAttributes(id, TEST_MAP));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectBodiesHeaders(request, endpoint, this.mockServer.getBaseUri(),
        RequestType.POST);
    assertSentCorrectJson(request, TEST_MAP_JSON);
  }

  @Test
  public void getCustomerAttributes_success() throws Exception {
    String endpoint = assertCustomerAttributesGuts(RequestType.GET,
        (facade, id) -> facade.getCustomerAttributes(id));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectBodiesHeaders(request, endpoint, this.mockServer.getBaseUri(),
        RequestType.GET);
  }
}
