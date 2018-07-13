package com.ic.invoicecapture;

import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Company;
import com.ic.invoicecapture.model.builder.CompanyBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.javatuples.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcFacadeIT {

  private static final String TEST_API_TOKEN = "1234567890abcdef";

  private MockServerFacade mockServer;

  @BeforeEach
  private void startServer() {
    mockServer = new MockServerFacade();
  }

  @AfterEach
  private void closeServer() throws IOException {
    mockServer.close();
  }


  private Pair<MockResponse, Company> initCompanyConfiguration() {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    String companyJson = companyBuilder.buildJsonObject().toString();

    MockResponse mockResponse =
        this.mockServer.addMockResponse("Content-Type", "application/json", companyJson);

    Company correctCompany = companyBuilder.buildCompany();

    return Pair.with(mockResponse, correctCompany);
  }

  private void assertSentCorrectGet() throws InterruptedException {

    RecordedRequest request = this.mockServer.getRequest();
    MockServerFacade.assertApiEndpointHit(request, IcFacade.COMPANIES_ENDPOINT);
    MockServerFacade.assertRequestLineContains(request, "GET");
    MockServerFacade.assertHeaderContainsValue(request, "X-Api-Token", TEST_API_TOKEN);
    MockServerFacade.assertHeaderContainsValue(request, "Content-Type", "application/json");
    MockServerFacade.assertHeaderContainsValue(request, "Accept", "application/json");
    MockServerFacade.assertHeaderContainsValue(request, "Content-Type", "utf-8");
  }

  private void assertReceivedCorrectJson(Company correctCompany) throws IcException {
    URI baseUri = this.mockServer.getBaseUri();
    IcFacade icFacade = new IcFacade(TEST_API_TOKEN, baseUri);
    Company returnedCompany = icFacade.requestCompanyInfo();
    Assertions.assertEquals(correctCompany, returnedCompany);
  }

  @Test
  public void requestCompanyInfo_successNormalConditions()
      throws IcException, IOException, InterruptedException {

    Company correctCompany = this.initCompanyConfiguration().getValue1();

    this.mockServer.start();
    this.assertReceivedCorrectJson(correctCompany);

    this.assertSentCorrectGet();
  }

  @Test
  public void requestCompanyInfo_slowConnection()
      throws IOException, IcException, InterruptedException {
    Pair<MockResponse,Company> pair = this.initCompanyConfiguration();
    Company correctCompany = pair.getValue1();
    MockResponse response = pair.getValue0();
    
    response.throttleBody(1024, 1, TimeUnit.SECONDS);

    this.mockServer.start();
    this.assertReceivedCorrectJson(correctCompany);

    this.assertSentCorrectGet();
  }
}
