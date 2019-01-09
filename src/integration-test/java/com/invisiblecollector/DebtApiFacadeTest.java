package com.invisiblecollector;

import com.invisiblecollector.connection.RequestType;
import com.invisiblecollector.model.Debt;
import com.invisiblecollector.model.FindDebtsBuilder;
import com.invisiblecollector.model.builder.DebtBuilder;
import com.invisiblecollector.model.builder.FindDebtsBuilderBuilder;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DebtApiFacadeTest extends IcFacadeTestBase {

  private static final String DEBTS_ENDPOINT = "debts";

  @Test
  public void registerNewCustomer_success() throws Exception {
    DebtBuilder debtBuilder = DebtBuilder.buildTestDebtBuilder();
    IcApiFacade icFacade = initJsonResponseMock(debtBuilder);

    this.assertCorrectModelReturned(debtBuilder, (Debt debt) -> icFacade.registerNewDebt(debt));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectCoreHeaders(
        request, DEBTS_ENDPOINT, this.mockServer.getBaseUri(), RequestType.POST);
    assertSentCorrectJson(
        request,
        DEBTS_ENDPOINT,
        this.mockServer.getBaseUri(),
        RequestType.POST,
        debtBuilder.buildSendableJson(false));
  }

  @Test
  public void requestDebtInfo_success() throws Exception {
    DebtBuilder debtBuilder = DebtBuilder.buildTestDebtBuilder();
    IcApiFacade icFacade = initJsonResponseMock(debtBuilder);
    String endpoint = DEBTS_ENDPOINT + "/" + debtBuilder.getId();

    this.assertCorrectModelReturned(
        debtBuilder, (Debt unused) -> icFacade.requestDebtInfo(debtBuilder.getId()));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectCoreHeaders(
        request, endpoint, this.mockServer.getBaseUri(), RequestType.GET);
  }

  @Test
  public void findDebts_success() throws Exception {
    Pair<List<Debt>, String> pair = DebtBuilder.buildTestDebtList();
    String json = pair.second;
    List<Debt> debts = pair.first;

    MockResponse mockResponse = buildBodiedJsonMockResponse(json);
    IcApiFacade icFacade = initMockServer(mockResponse);

    FindDebtsBuilderBuilder builderBuilder = FindDebtsBuilderBuilder.buildTestBuilder();
    FindDebtsBuilder builder = builderBuilder.buildModel();

    List<Debt> returnedDebts = icFacade.findDebts(builder);

    assertObjectsEquals(debts, returnedDebts);
    RecordedRequest request = this.mockServer.getRequest();
    String endpoint = String.format("debts/find?%s", builderBuilder.buildSendableUrlQuery());
    this.assertSentCorrectCoreHeaders(
        request, endpoint, this.mockServer.getBaseUri(), RequestType.GET);
  }
}
