package com.ic.invoicecapture.model.json;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.ICompanyUpdate;
import com.ic.invoicecapture.model.ICustomerUpdate;
import com.ic.invoicecapture.model.IModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Thread-safe.
 * 
 * @author ros
 *
 */
public class JsonModelFacade {
  
  private interface StringMap extends Map<String, String> {}

  public <T> T parseStringStream(InputStream inputStream, Class<T> classType) throws IcException {

    InputStreamReader inputStreamReader =
        new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    JsonReader reader = new JsonReader(inputStreamReader);
    Gson gson = GsonSingleton.getInstance();
    T value;
    try {
      value = gson.fromJson(reader, classType);
    } catch (JsonIOException | JsonSyntaxException e) {
      throw new IcException("Failed to parse Json");
    }
    try {
      reader.close();
    } catch (IOException e) {
      throw new IcException(e);
    }
    return value;
  }
  
  public Map<String, String> parseStringStreamAsStringMap(InputStream inputStream) throws IcException {
    return parseStringStream(inputStream, StringMap.class);
  }

  public String toJson(IModel model) {
    final Gson gson = GsonSingleton.getInstance();
    return gson.toJson(model);
  }

  public String toJson(ICompanyUpdate model) {

    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("name", model.getName());
    jsonObj.addProperty("vatNumber", model.getVatNumber());
    jsonObj.addProperty("address", model.getAddress());
    jsonObj.addProperty("zipCode", model.getZipCode());
    jsonObj.addProperty("city", model.getCity());
    return jsonObj.toString();
  }

  public String toJson(ICustomerUpdate model) {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("name", model.getName());
    jsonObj.addProperty("externalId", model.getExternalId());
    jsonObj.addProperty("vatNumber", model.getVatNumber());
    jsonObj.addProperty("address", model.getAddress());
    jsonObj.addProperty("zipCode", model.getZipCode());
    jsonObj.addProperty("city", model.getCity());
    jsonObj.addProperty("country", model.getCountry());
    jsonObj.addProperty("email", model.getEmail());
    jsonObj.addProperty("phone", model.getPhone());
    return jsonObj.toString();
  }

  public String toJson(Map<String, String> attributes) {
    JsonObject jsonObject = new JsonObject();
    for (Map.Entry<String, String> entry : attributes.entrySet()) {
      jsonObject.addProperty(entry.getKey(), entry.getValue());
    }

    return jsonObject.toString();
  }

  
}
