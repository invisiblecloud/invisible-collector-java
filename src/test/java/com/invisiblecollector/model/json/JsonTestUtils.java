package com.invisiblecollector.model.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

public class JsonTestUtils {
 
  public static void assertJsonEquals(String expectedJson, String actualJson) {
    ObjectMapper mapper = JsonSingleton.getInstance();

    try {
      JsonNode expected = mapper.readTree(expectedJson);
      JsonNode actual = mapper.readTree(actualJson);
      Assertions.assertEquals(expected, actual);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
