package com.invisiblecollector.model.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.Model;

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
      throw new IcException("Failed to parse Json");
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
