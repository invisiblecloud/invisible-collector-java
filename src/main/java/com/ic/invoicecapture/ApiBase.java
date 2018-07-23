package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import com.ic.invoicecapture.connection.builders.IThrowingBuilder;
import com.ic.invoicecapture.connection.response.validators.IValidator;
import com.ic.invoicecapture.connection.response.validators.ValidatorBuilder;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.IInternallyRoutable;
import com.ic.invoicecapture.model.json.JsonModelFacade;
import java.io.InputStream;
import java.net.URI;

public abstract class ApiBase {

  protected ApiRequestFacade apiFacade;
  protected JsonModelFacade jsonFacade;
  protected ValidatorBuilder validatorBuilder;

  protected ApiBase(ApiRequestFacade apiFacade, JsonModelFacade jsonFacade,
      ValidatorBuilder validatorBuilder) {
    this.apiFacade = apiFacade;
    this.jsonFacade = jsonFacade;
    this.validatorBuilder = validatorBuilder;

    this.validatorBuilder.addCommonValidators();
  }

  protected ApiBase(String apiToken, URI baseUrl) {
    this(new ApiRequestFacade(apiToken, baseUrl));
  }

  protected ApiBase(ApiRequestFacade apiFacade) {
    this(apiFacade, new JsonModelFacade(), new ValidatorBuilder());
  }

  protected void assertCorrectId(String id) throws IllegalArgumentException {
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("Id cannot be null");
    }
  }

  protected String getAndAssertCorrectId(IInternallyRoutable idContainer)
      throws IllegalArgumentException {
    String gid = idContainer.getId();
    String externalId = idContainer.getExternalId();
    if (gid != null && !gid.isEmpty()) {
      return gid;
    } else if (externalId != null && !externalId.isEmpty()) {
      return externalId;
    } else {
      throw new IllegalArgumentException("no valid id contained in object");
    }
  }

  protected <T> T returningRequest(Class<T> returnType, ValidatorBuilder validatorBuilder,
      IThrowingBuilder<InputStream, IValidator> requestMethod) throws IcException {
    IValidator validator = validatorBuilder.addServerJsonValidator().build();
    InputStream inputStream = requestMethod.build(validator);

    return this.jsonFacade.parseStringStream(inputStream, returnType);
  }

}
