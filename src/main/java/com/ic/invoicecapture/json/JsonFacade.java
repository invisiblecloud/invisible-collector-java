package com.ic.invoicecapture.json;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Thread-safe.
 * 
 * @author ros
 *
 */
public class JsonFacade {

  private static final String STRING_ENCODING = "UTF-8";

  public <T> T stringStreamToJsonObject(InputStream inputStream, Class<T> classType)
      throws IcException {

    InputStreamReader inputStreamReader;
    try {
      inputStreamReader = new InputStreamReader(inputStream, STRING_ENCODING);
    } catch (UnsupportedEncodingException e) {
      throw new IcException(e);
    }
    JsonReader reader = new JsonReader(inputStreamReader);
    Gson gson = GsonSingleton.getInstance();
    return gson.fromJson(reader, classType);
  }
}
