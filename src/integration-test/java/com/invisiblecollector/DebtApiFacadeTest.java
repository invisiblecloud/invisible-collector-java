package com.invisiblecollector;

import org.junit.jupiter.api.Test;

import com.invisiblecollector.connection.RequestType;
import com.invisiblecollector.model.Debt;
import com.invisiblecollector.model.builder.DebtBuilder;

import okhttp3.mockwebserver.RecordedRequest;

public class DebtApiFacadeTest extends IcFacadeTestBase {

  private static final String DEBTS_ENDPOINT = "debts";

  private DebtApiFacade buildDebtResponseAndAddServerReply(DebtBuilder debtBuilder)
          throws Exception {
    return buildIcApiResponseAndAddServerReply(debtBuilder).getDebtFacade();
  }

  @Test
  public void registerNewCustomer_success() throws Exception {
    DebtBuilder debtBuilder = DebtBuilder.buildTestDebtBuilder();
    DebtApiFacade icFacade = buildDebtResponseAndAddServerReply(debtBuilder);

    this.assertCorrectModelReturned(debtBuilder, (Debt debt) -> icFacade.registerNewDebt(debt));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, DEBTS_ENDPOINT,
        this.mockServer.getBaseUri(), RequestType.POST);
    assertSentCorrectJson(request, debtBuilder.buildSendableJson());
  }

  @Test
  public void requestDebtInfo() throws Exception {
    DebtBuilder debtBuilder = DebtBuilder.buildTestDebtBuilder();
    DebtApiFacade icFacade = buildDebtResponseAndAddServerReply(debtBuilder);
    String endpoint = DEBTS_ENDPOINT + "/" + debtBuilder.getId();

    this.assertCorrectModelReturned(debtBuilder,
        (Debt unused) -> icFacade.requestDebtInfo(debtBuilder.getId()));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, endpoint, this.mockServer.getBaseUri(),
        RequestType.GET);
  }
}
