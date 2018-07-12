package com.ic.invoicecapture;

import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Company;
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
  public void requestCompanyInfo_successNormalConditions()
      throws IOException, InterruptedException {
    MockWebServer server = new MockWebServer();

    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    String companyJson = companyBuilder.buildJsonObject().toString();


    MockResponse mockResponse =
        new MockResponse().setHeader("Content-Type", "application/json").setBody(companyJson);

    server.enqueue(mockResponse);

    server.start();

    URI baseUrl = server.url("").uri();

    IcFacade icFacade = new IcFacade(TEST_API_TOKEN, baseUrl);
    Company returnedCompany;

    try {
      returnedCompany = icFacade.requestCompanyInfo();
    } catch (IcException e) {
      server.shutdown();
      Assertions.fail(e);
      return; // redundant
    }

    Company correctCompany = companyBuilder.buildCompany();
    Assertions.assertEquals(correctCompany, returnedCompany);

    RecordedRequest request = server.takeRequest();
    Assertions.assertEquals("/" + IcFacade.COMPANIES_ENDPOINT, request.getPath());

    server.shutdown();
  }
}
