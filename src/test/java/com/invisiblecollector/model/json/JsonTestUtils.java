package com.invisiblecollector.model.json;

import org.junit.jupiter.api.Assertions;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonTestUtils {
  public static final JsonObject jsonStringAsJsonObject(String json) {
    JsonParser parser = new JsonParser();
    return parser.parse(json).getAsJsonObject();
  }
 
  public static void assertJsonEquals(String expectedJson, String actualJson) {
    Gson gson = GsonSingleton.getInstance();
    JsonElement expected = gson.fromJson(expectedJson, JsonElement.class);
    JsonElement obj = gson.fromJson(actualJson, JsonElement.class);
    Assertions.assertEquals(expected, obj);
  }
  
  public static void assertObjectsEqualsAsJson(Object expected, Object actual) {
    Gson gson = GsonSingleton.getInstance();
    String expectedJson = gson.toJson(expected);
    String actualJson = gson.toJson(actual);

    assertJsonEquals(expectedJson, actualJson);
  }
  
}
