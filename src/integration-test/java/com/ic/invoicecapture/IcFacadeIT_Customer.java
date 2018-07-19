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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IcFacadeIT_Customer extends IcFacadeTestBase {

  // same as TEST_MAP in json form
  private static String TEST_MAP_JSON = "{ \"a\":\"12\", \"b\":\"string\" }";
  private static Map<String, String> TEST_MAP = new TreeMap<>();

  static {
    TEST_MAP.put("a", "12");
    TEST_MAP.put("b", "string");
  }

  public IcFacade buildCustomerResponse(CustomerBuilder customerBuilder) throws Exception {
    String json = customerBuilder.buildJson();
    MockResponse mockResponse = buildBodiedMockResponse(json);
    return initMockServer(mockResponse);
  }

  public void assertCorrectCustomerReturned(CustomerBuilder customerBuilder,
      IThrowingBuilder<Customer, Customer> method) throws IcException {
    Customer correctCustomer = customerBuilder.buildCustomer();
    Customer returnedCustomer = method.build(correctCustomer);
    Assertions.assertEquals(correctCustomer, returnedCustomer);
  }

  @Test
  public void registerNewCustomer_success() throws Exception {
    CustomerBuilder customerBuilder = CustomerBuilder.buildTestCustomerBuilder();
    IcFacade icFacade = buildCustomerResponse(customerBuilder);

    this.assertCorrectCustomerReturned(customerBuilder,
        (customer) -> icFacade.registerNewCustomer(customer));
    this.assertSentCorrectPostHeaders(IcFacade.CUSTOMERS_ENDPOINT, this.mockServer.getBaseUri());
  }

  @Test
  public void registerNewCustomer_conflict() throws Exception {
    int statusCode = 409;
    String json = buildConflictErrorJson(statusCode);
    MockResponse mockResponse = buildBodiedMockResponse(json).setResponseCode(statusCode);
    IcFacade icFacade = initMockServer(mockResponse);
    Customer correctCustomer = CustomerBuilder.buildTestCustomerBuilder().buildCustomer();
    IcConflictingException exception = Assertions.assertThrows(IcConflictingException.class,
        () -> icFacade.registerNewCustomer(correctCustomer));
    Assertions.assertEquals(CONFLICT_GID, exception.getGid());
  }

  @Test
  public void updateCustomerInfo_success() throws Exception {
    CustomerBuilder customerBuilder =
        CustomerBuilder.buildTestCustomerBuilder().setName("Brand new new Name");
    String id = customerBuilder.getExternalId();
    String endpoint = joinUriPaths(IcFacade.CUSTOMERS_ENDPOINT, id);
    IcFacade icFacade = buildCustomerResponse(customerBuilder);

    this.assertCorrectCustomerReturned(customerBuilder, (unused) -> {
      Customer updateCustomer =
          customerBuilder.setVatNumber(null).setExternalId(null).buildCustomer();
      return icFacade.updateCustomerInfo(updateCustomer, id);
    });
    this.assertSentCorrectPutHeaders(endpoint, this.mockServer.getBaseUri());
  }

  @Test
  public void requestCustomerInfo_success() throws Exception {
    CustomerBuilder customerBuilder = CustomerBuilder.buildTestCustomerBuilder();
    IcFacade icFacade = buildCustomerResponse(customerBuilder);
    String id = customerBuilder.getExternalId();
    String endpoint = joinUriPaths(IcFacade.CUSTOMERS_ENDPOINT, id);
    this.assertCorrectCustomerReturned(customerBuilder,
        (customer) -> icFacade.requestCustomerInfo(id));
    this.assertSentCorrectGetHeaders(endpoint, this.mockServer.getBaseUri());
  }

  private void assertCustomerAttributesGuts(RequestType requestType,
      IThrowingBuilder2<Map<String, String>, IcFacade, String> method) throws Exception {
    MockResponse response = buildBodiedMockResponse(TEST_MAP_JSON);
    IcFacade facade = initMockServer(response);
    String id = "123";
    String endpoint = joinUriPaths(IcFacade.CUSTOMERS_ENDPOINT, id, IcFacade.ATTRIBUTES_PATH);
    Map<String, String> returnedMap = method.build(facade, id);
    Assertions.assertEquals(TEST_MAP, returnedMap);
    this.assertSentCorrectHeaders(endpoint, this.mockServer.getBaseUri(), requestType);
  }

  @Test
  public void setCustomerAttributes_success() throws Exception {
    assertCustomerAttributesGuts(RequestType.POST,
        (facade, id) -> facade.setCustomerAttributes(id, TEST_MAP));
  }

  @Test
  public void getCustomerAttributes_success() throws Exception {
    assertCustomerAttributesGuts(RequestType.GET, (facade, id) -> facade.getCustomerAttributes(id));
  }



}
