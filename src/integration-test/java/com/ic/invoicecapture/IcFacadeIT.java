package com.ic.invoicecapture;

import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.builder.CompanyBuilder;
import java.io.IOException;
import java.net.URI;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IcFacadeIT {

  private static final String TEST_API_TOKEN = "1234567890abcdef";

  @Test
  public void requestCompanyInfo_passNormalConditions() throws IOException {
    MockWebServer server = new MockWebServer();

    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    String companyJson = companyBuilder.buildJsonObject().toString();

    MockResponse mockResponse = new MockResponse();
    mockResponse.setBody(companyJson);
    server.enqueue(mockResponse);

    server.start();

    URI baseUrl = server.url("").uri();

    IcFacade icFacade = new IcFacade(TEST_API_TOKEN, baseUrl);

    try {
      icFacade.requestCompanyInfo();
    } catch (IcException e) {
      server.shutdown();
      Assertions.fail(e);
    }


    // Optional: confirm that your app made the HTTP requests you were expecting.
    // RecordedRequest request1 = server.takeRequest();
    // assertEquals("/v1/chat/messages/", request1.getPath());
    // assertNotNull(request1.getHeader("Authorization"));
    //
    // RecordedRequest request2 = server.takeRequest();
    // assertEquals("/v1/chat/messages/2", request2.getPath());
    //
    // RecordedRequest request3 = server.takeRequest();
    // assertEquals("/v1/chat/messages/3", request3.getPath());
    //

    server.shutdown();
  }
}
