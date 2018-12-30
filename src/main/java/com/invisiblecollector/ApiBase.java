package com.invisiblecollector;

import com.invisiblecollector.connection.ApiRequestFacade;
import com.invisiblecollector.connection.ResponseValidator;
import com.invisiblecollector.connection.builders.ThrowingSupplier;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.json.JsonModelFacade;
import java.io.InputStream;
import java.net.URI;

public abstract class ApiBase {

  protected ApiRequestFacade apiFacade;
  protected JsonModelFacade jsonFacade;

  protected ApiBase(
          ApiRequestFacade apiFacade, JsonModelFacade jsonFacade) {
    this.apiFacade = apiFacade;
    this.jsonFacade = jsonFacade;
  }

  protected ApiBase(String apiToken, URI baseUrl) {
    this.jsonFacade = new JsonModelFacade();
    ResponseValidator responseValidator = new ResponseValidator(this.jsonFacade);
    this.apiFacade = new ApiRequestFacade(apiToken, baseUrl, responseValidator);
  }

  protected ApiBase(ApiRequestFacade apiFacade) {
    this(apiFacade, new JsonModelFacade());
  }

  protected void assertCorrectId(String id) throws IllegalArgumentException {
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("Id cannot be null");
    }
  }

  protected <T> T returningRequest(
      Class<T> returnType, ThrowingSupplier<InputStream, IcException> requestMethod)
      throws IcException {
    InputStream inputStream = requestMethod.get();
    return this.jsonFacade.parseStringStream(inputStream, returnType);
  }
}
