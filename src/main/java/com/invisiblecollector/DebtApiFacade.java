package com.invisiblecollector;

import com.invisiblecollector.connection.ApiRequestFacade;
import com.invisiblecollector.connection.response.validators.ValidatorBuilder;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.Debt;
import com.invisiblecollector.model.DebtField;
import com.invisiblecollector.model.IRoutable;
import java.net.URI;
import java.util.Map;

/**
 * Immutable and thread safe class for making operations on the {@code /debts } API endpoint.
 * 
 * <p>For object construction see {@link IcApiFacade}
 * 
 * @author ros
 */
public class DebtApiFacade extends ApiBase {

  private static final String DEBTS_ENDPOINT = "debts";

  public DebtApiFacade(ApiRequestFacade apiFacade) {
    super(apiFacade);
  }

  public DebtApiFacade(String apiToken, URI baseUrl) {
    super(apiToken, baseUrl);
  }

  /**
   * Register a new debt related to a customer.
   * 
   * <p>See {@link #registerNewDebt(Map)} for more details
   * 
   * @param debt the debt information to register. null values are discarded.
   */
  public Debt registerNewDebt(Debt debt) throws IcException {
    Map<DebtField, Object> debtMap = debt.toEnumMap();
    return this.registerNewDebt(debtMap);
  }

  /**
   * Register a new debt related to a customer.
   * 
   * @param debtInfo a map containing the debt information to register. 
   *        null values are <b>not</b> discarded. See {@link DebtField} for 
   *        a description of the attributes and their possible values. 
   *        See {@link DebtField#assertCorrectlyInitialized(Map)} for a list of the 
   *        <b>mandatory</b> attributes.
   * @return an up-to-date object with the debt information. 
   * @throws IcException any general exception
   * @see #registerNewDebt(Debt)
   */
  public Debt registerNewDebt(Map<DebtField, Object> debtInfo) throws IcException {
    DebtField.assertCorrectlyInitialized(debtInfo);
    String jsonToSend = this.jsonFacade.toJson(debtInfo);
    ValidatorBuilder builder = this.validatorBuilder.clone().addBadClientJsonValidator()
        .addConflictValidator("A debt already exists with the same id");

    return this.returningRequest(Debt.class, builder,
        (validator) -> apiFacade.postRequest(validator, DEBTS_ENDPOINT, jsonToSend));
  }

  /**
   * Request debt information from the database.
   * 
   * <p>See {@link #requestDebtInfo(String)} for more details
   * 
   * @param routable an object (such as a {@link Debt} object) containing the id of the debt.
   */
  public Debt requestDebtInfo(IRoutable routable) throws IcException {
    return requestDebtInfo(routable.getRoutableId());
  }
  
  /**
   * Request debt information from the database.
   * 
   * @param debtId the id of the debt whose information is requested.
   * @return up-to-date debt information.
   * @throws IcException any general exception
   * @see #requestDebtInfo(IRoutable)
   */
  public Debt requestDebtInfo(String debtId) throws IcException {
    assertCorrectId(debtId);
    ValidatorBuilder builder = this.validatorBuilder.clone();
    String endpoint = DEBTS_ENDPOINT + "/" + debtId;
    return this.returningRequest(Debt.class, builder,
        (validator) -> apiFacade.getRequest(validator, endpoint));
  }
}


