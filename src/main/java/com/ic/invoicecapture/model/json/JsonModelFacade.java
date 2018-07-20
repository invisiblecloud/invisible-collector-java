package com.ic.invoicecapture.model.json;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    InputStreamReader inputStreamReader =
        new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    JsonReader reader = new JsonReader(inputStreamReader);
    Gson gson = GsonSingleton.getInstance();
    T value;
    try {
      value = gson.fromJson(reader, classType);
    } catch (JsonIOException | JsonSyntaxException e) {
      throw new IcException("Failed to parse Json");
    }
    try {
      reader.close();
    } catch (IOException e) {
      throw new IcException(e);
    }
    return value;
  }

  public Map<String, String> parseStringStreamAsStringMap(InputStream inputStream)
      throws IcException {
    return parseStringStream(inputStream, StringMap.class);
  }

  public String toJson(Object obj) {
    return GsonSingleton.getInstance().toJson(obj);
  }


}
