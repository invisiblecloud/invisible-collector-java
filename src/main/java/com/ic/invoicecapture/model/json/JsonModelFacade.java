package com.ic.invoicecapture.model.json;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.ICompanyUpdate;
import com.ic.invoicecapture.model.IModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Thread-safe.
 * 
 * @author ros
 *
 */
public class JsonModelFacade {

  public <T> T parseStringStream(InputStream inputStream, Class<T> classType)
      throws IcException {

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
  
  
  
}
