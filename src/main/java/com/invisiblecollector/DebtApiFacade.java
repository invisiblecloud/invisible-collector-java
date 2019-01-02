package com.invisiblecollector;

import com.invisiblecollector.connection.ApiRequestFacade;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.Debt;
import com.invisiblecollector.model.DebtField;

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
   * @param debtInfo the debt information to register. See {@link DebtField} for a description of
   *     the attributes and their possible values. The {@code number}, {@code customerId}, {@code
   *     type}, {@code date} and {@code dueDate} are <b>mandatory</b> attributes. If the model
   *     contains items they must contain the {@code name} attribute.
   * @return an up-to-date object with the debt information.
   * @throws IcException any general exception
   * @see #registerNewDebt(Debt)
   */
  public Debt registerNewDebt(Debt debtInfo) throws IcException {
    debtInfo.assertConstainsKeys("number", "customerId", "type", "date", "dueDate");
    Map<String, Object> fields = debtInfo.getOnlyFields("number", "customerId", "type", "status", "date", "dueDate", "netTotal", "tax", "grossTotal", "currency", "items", "attributes");
    debtInfo.getItems().stream().forEach(item -> item.assertConstainsKeys("name"));
    String jsonToSend = this.jsonFacade.toJson(fields);

    return this.returningRequest(Debt.class,
            () -> apiFacade.postRequest(DEBTS_ENDPOINT, jsonToSend));
  }

  /**
   * Request debt information from the database.
   *
   * @param debtId the id of the debt whose information is requested.
   * @return up-to-date debt information.
   * @throws IcException any general exception
   */
  public Debt requestDebtInfo(String debtId) throws IcException {
    assertCorrectId(debtId);

    String endpoint = DEBTS_ENDPOINT + "/" + debtId;
    return this.returningRequest(Debt.class,
            () -> apiFacade.getRequest(endpoint));
  }
}


