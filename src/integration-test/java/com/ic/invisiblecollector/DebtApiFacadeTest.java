package com.ic.invisiblecollector;

import com.ic.invisiblecollector.DebtApiFacade;
import com.ic.invisiblecollector.connection.RequestType;
import com.ic.invisiblecollector.model.Debt;
import com.ic.invisiblecollector.model.builder.DebtBuilder;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

public class DebtApiFacadeTest extends IcFacadeTestBase {

  private static final String DEBTS_ENDPOINT = "debts";
  
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

  private DebtApiFacade buildDebtResponseAndAddServerReply(DebtBuilder debtBuilder)
      throws Exception {
    return buildIcApiResponseAndAddServerReply(debtBuilder).getDebtFacade();
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
