package com.ic.invisiblecollector;

import com.ic.invisiblecollector.connection.ApiRequestFacade;
import com.ic.invisiblecollector.connection.builders.IThrowingBuilder;
import com.ic.invisiblecollector.connection.response.validators.IValidator;
import com.ic.invisiblecollector.connection.response.validators.ValidatorBuilder;
import com.ic.invisiblecollector.exceptions.IcException;
import com.ic.invisiblecollector.model.json.JsonModelFacade;
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

  protected <T> T returningRequest(Class<T> returnType, ValidatorBuilder validatorBuilder,
      IThrowingBuilder<InputStream, IValidator> requestMethod) throws IcException {
    IValidator validator = validatorBuilder.addServerJsonValidator().build();
    InputStream inputStream = requestMethod.build(validator);

    return this.jsonFacade.parseStringStream(inputStream, returnType);
  }

}
