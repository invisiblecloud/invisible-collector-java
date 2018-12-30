package com.invisiblecollector;

import com.invisiblecollector.connection.ApiRequestFacade;
import com.invisiblecollector.connection.ResponseValidator;
import com.invisiblecollector.connection.builders.IThrowingBuilder;
import com.invisiblecollector.connection.response.validators.IValidator;
import com.invisiblecollector.connection.response.validators.ValidatorBuilder;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.json.JsonModelFacade;
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
    this.jsonFacade = new JsonModelFacade();
    ResponseValidator responseValidator = new ResponseValidator(this.jsonFacade);
    this.apiFacade = new ApiRequestFacade(apiToken, baseUrl, responseValidator);
    this.validatorBuilder = new ValidatorBuilder();

    this.validatorBuilder.addCommonValidators();
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
