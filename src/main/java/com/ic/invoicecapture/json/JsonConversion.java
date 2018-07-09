package com.ic.invoicecapture.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class JsonConversion {
  public static final JsonObject jsonStringAsJsonObject(String json) {
    JsonParser parser = new JsonParser();
    return parser.parse(json).getAsJsonObject();
  }

  private static final String STRING_ENCODING = "UTF-8";

  public static final <T> T stringStreamToJsonObject(InputStream inputStream, Class<T> classType)
      throws UnsupportedEncodingException {
    JsonReader reader = new JsonReader(new InputStreamReader(inputStream, STRING_ENCODING));
    Gson gson = GsonSingleton.getInstance();
    return gson.fromJson(reader, classType);
  }
}
