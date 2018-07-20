package com.ic.invoicecapture.model.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;

public class JsonTestUtils {
  public static final JsonObject jsonStringAsJsonObject(String json) {
    JsonParser parser = new JsonParser();
    return parser.parse(json).getAsJsonObject();
  }
 
  public static void assertJsonEquals(String expectedJson, String returnedJson) {
    Gson gson = GsonSingleton.getInstance();
    JsonElement expected = gson.fromJson(expectedJson, JsonElement.class);
    JsonElement obj = gson.fromJson(returnedJson, JsonElement.class);
    Assertions.assertEquals(expected, obj);
  }
  
}
