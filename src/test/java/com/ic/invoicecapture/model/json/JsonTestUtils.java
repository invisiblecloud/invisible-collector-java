package com.ic.invoicecapture.model.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;

public class JsonTestUtils {
  public static final JsonObject jsonStringAsJsonObject(String json) {
    JsonParser parser = new JsonParser();
    return parser.parse(json).getAsJsonObject();
  }
 
  public static void assertJsonEquals(String expectedJson, String json) {
    JsonParser parser = new JsonParser();
    JsonElement expected = parser.parse(expectedJson);
    JsonElement obj = parser.parse(json);
    Assertions.assertEquals(expected, obj);
  }
  
}
