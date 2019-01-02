package com.invisiblecollector.model.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.Debt;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Thread-safe.
 *
 * @author ros
 */
public class JsonModelFacade {

  private static final String PARSING_ERROR_MSG = "Failed to parse JSON.";

  public <T> T parseStringStream(InputStream inputStream, Class<T> classType) throws IcException {

    try {
      return JsonSingleton.getInstance2().readValue(inputStream, classType);
    } catch (IOException e) {
      throw new IcException(PARSING_ERROR_MSG, e);
    }
  }

  public Map<String, String> parseStringStreamAsStringMap(InputStream inputStream)
      throws IcException {
    TypeReference<Map<String, String>> valueTypeRef = new TypeReference<Map<String, String>>() {};
    return parseStringStreamAsCollection(inputStream, valueTypeRef);
  }

  public List<Debt> parseStringStreamAsDebtList(InputStream inputStream)
          throws IcException {
    TypeReference<List<Debt>> typeReference = new TypeReference<List<Debt>>() {};
    return parseStringStreamAsCollection(inputStream, typeReference);
  }

  private <T> T parseStringStreamAsCollection(InputStream inputStream, TypeReference<T> valueTypeRef)
      throws IcException {
    try {
      return JsonSingleton.getInstance2().readValue(inputStream, valueTypeRef);
    } catch (IOException e) {
      throw new IcException(PARSING_ERROR_MSG, e);
    }
  }

  public String toJson(Object obj) {
    try {
      return JsonSingleton.getInstance2().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
