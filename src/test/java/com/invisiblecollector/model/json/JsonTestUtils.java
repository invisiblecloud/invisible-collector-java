package com.invisiblecollector.model.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class JsonTestUtils {
  public static final JsonObject jsonStringAsJsonObject(String json) {
    JsonParser parser = new JsonParser();
    return parser.parse(json).getAsJsonObject();
  }
 
  public static void assertJsonEquals(String expectedJson, String actualJson) {
    ObjectMapper mapper = JsonSingleton.getInstance2();

    try {
      JsonNode expected = mapper.readTree(expectedJson);
      JsonNode actual = mapper.readTree(actualJson);
      Assertions.assertEquals(expected, actual);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
  
  public static void assertObjectsEqualsAsJson(Object expected, Object actual) {
    Gson gson = JsonSingleton.getInstance();
    String expectedJson = gson.toJson(expected);
    String actualJson = gson.toJson(actual);

    assertJsonEquals(expectedJson, actualJson);
  }
  
}
