package com.ic.invisiblecollector;

import com.ic.invisiblecollector.connection.ApiRequestFacade;
import com.ic.invisiblecollector.connection.response.validators.ValidatorBuilder;
import com.ic.invisiblecollector.exceptions.IcException;
import com.ic.invisiblecollector.model.Debt;
import com.ic.invisiblecollector.model.DebtField;
import com.ic.invisiblecollector.model.IRoutable;
import java.net.URI;
import java.util.Map;

public class DebtApiFacade extends ApiBase {

  private static final String DEBTS_ENDPOINT = "debts";

  public DebtApiFacade(ApiRequestFacade apiFacade) {
    super(apiFacade);
  }

  public DebtApiFacade(String apiToken, URI baseUrl) {
    super(apiToken, baseUrl);
  }

  public Debt registerNewDebt(Debt debt) throws IcException {
    Map<DebtField, Object> debtMap = debt.toEnumMap();
    return this.registerNewDebt(debtMap);
  }

  public Debt registerNewDebt(Map<DebtField, Object> debtInfo) throws IcException {
    DebtField.assertCorrectlyInitialized(debtInfo);
    String jsonToSend = this.jsonFacade.toJson(debtInfo);
    ValidatorBuilder builder = this.validatorBuilder.clone().addBadClientJsonValidator()
        .addConflictValidator("A debt already exists with the same id");

    return this.returningRequest(Debt.class, builder,
        (validator) -> apiFacade.postRequest(validator, DEBTS_ENDPOINT, jsonToSend));
  }

  public Debt requestDebtInfo(IRoutable routable) throws IcException {
    return requestDebtInfo(routable.getId());
  }
  
  public Debt requestDebtInfo(String id) throws IcException {
    assertCorrectId(id);
    ValidatorBuilder builder = this.validatorBuilder.clone();
    String endpoint = DEBTS_ENDPOINT + "/" + id;
    return this.returningRequest(Debt.class, builder,
        (validator) -> apiFacade.getRequest(validator, endpoint));
  }
}


