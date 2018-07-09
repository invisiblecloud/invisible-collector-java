package com.ic.invoicecapture.json;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonConverter {
  public static final JsonObject jsonStringAsJsonObject(String json) {
    JsonParser parser = new JsonParser();
    return parser.parse(json).getAsJsonObject();
  }

}
