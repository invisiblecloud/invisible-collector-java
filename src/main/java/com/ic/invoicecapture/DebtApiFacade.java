package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import com.ic.invoicecapture.connection.response.validators.ValidatorBuilder;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Debt;
import com.ic.invoicecapture.model.DebtField;
import java.net.URI;
import java.util.Map;

public class DebtApiFacade extends ApiBase {
  
  public static final String DEBTS_ENDPOINT = "debts";
  
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

  public Debt registerNewDebt(Map<DebtField, Object> customerInfo) throws IcException {
    DebtField.assertCorrectlyInitialized(customerInfo);
    String jsonToSend = this.jsonFacade.toJson(customerInfo);
    ValidatorBuilder builder =
        this.validatorBuilder.clone().addBadClientJsonValidator().addConflictValidator();

    return this.returningRequest(Debt.class, builder,
        (validator) -> apiFacade.postRequest(validator, DEBTS_ENDPOINT, jsonToSend));
  }
  
  
}


