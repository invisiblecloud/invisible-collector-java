package com.invisiblecollector.connection.response.validators;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.invisiblecollector.exceptions.IcException;

/**
 * Immutable class.
 * 
 * @author ros
 *
 */
public class ServerErrorFacade {

  private JsonObject responseObject;

  public ServerErrorFacade(String json) throws IllegalArgumentException {
    JsonParser parser = new JsonParser();
    try {
      responseObject = parser.parse(json).getAsJsonObject();
    } catch (JsonParseException e) {
      throw new IllegalArgumentException("Failed to parse");
    }
  }

  public String getErrorMember(String key) throws IcException {
    JsonElement value = responseObject.get(key);
    if (value == null) {
      throw new IcException("key doesn't exist");
    } else {
      return value.getAsString();
    }
  }

  public String getErrorMessage() throws IcException {
    return getErrorMember("message");
  }
}
