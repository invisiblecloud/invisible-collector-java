package com.invisiblecollector.model.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.invisiblecollector.exceptions.IcException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Thread-safe.
 * 
 * @author ros
 *
 */
public class JsonModelFacade {

  // not to be used, just to get class of map
  private interface StringMap extends Map<String, String> {
  }

  public <T> T parseStringStream(InputStream inputStream, Class<T> classType) throws IcException {

    try {
      return JsonSingleton.getInstance2()
              .readValue(inputStream, classType);
    } catch (IOException e) {
      throw new IcException("Failed to parse JSON.");
    }
  }

  public Map<String, String> parseStringStreamAsStringMap(InputStream inputStream)
      throws IcException {
    return parseStringStream(inputStream, StringMap.class);
  }

  public String toJson(Object obj) {
    try {
      return JsonSingleton.getInstance2()
              .writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

}
