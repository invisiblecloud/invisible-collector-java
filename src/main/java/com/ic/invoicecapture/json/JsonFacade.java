package com.ic.invoicecapture.json;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Thread-safe.
 * 
 * @author ros
 *
 */
public class JsonFacade {

  public <T> T stringStreamToJsonObject(InputStream inputStream, Class<T> classType)
      throws IcException {

    InputStreamReader inputStreamReader =
        new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    JsonReader reader = new JsonReader(inputStreamReader);
    Gson gson = GsonSingleton.getInstance();
    T value = gson.fromJson(reader, classType);
    try {
      reader.close();
    } catch (IOException e) {
      throw new IcException(e);
    }
    return value;
  }
}
