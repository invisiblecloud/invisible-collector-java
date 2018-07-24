package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.RequestType;
import com.ic.invoicecapture.model.Debt;
import com.ic.invoicecapture.model.builder.DebtBuilder;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

public class DebtApiFacadeTest extends IcFacadeTestBase {
  
  @Test
  public void registerNewCustomer_success() throws Exception {
    DebtBuilder debtBuilder = DebtBuilder.buildTestDebtBuilder();
    DebtApiFacade icFacade = buildDebtResponse(debtBuilder);

    this.assertCorrectModelReturned(debtBuilder,
        (Debt debt) -> icFacade.registerNewDebt(debt));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectBodiesHeaders(request, DebtApiFacade.DEBTS_ENDPOINT,
        this.mockServer.getBaseUri(), RequestType.POST);
    assertSentCorrectJson(request, debtBuilder.buildSendableJson());
  }

  private DebtApiFacade buildDebtResponse(DebtBuilder debtBuilder) throws Exception {
    return buildIcApiResponse(debtBuilder).getDebtFacade();
  }
}
