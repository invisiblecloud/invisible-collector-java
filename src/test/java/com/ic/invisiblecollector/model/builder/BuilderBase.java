package com.ic.invisiblecollector.model.builder;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ic.invisiblecollector.model.IModel;
import com.ic.invisiblecollector.model.json.GsonSingleton;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class BuilderBase {
  public abstract IModel buildModel();

  protected <T> T buildModel(Class<T> classType) {
    String json = this.buildJsonObject().toString();

    Gson gson = GsonSingleton.getInstance();
    return gson.fromJson(json, classType);
  }
  
  public abstract JsonObject buildSendableJsonObject();

  public JsonObject buildSendableJsonObject(boolean stripNulls) {
    JsonObject obj = buildSendableJsonObject();

    if (stripNulls) {
      Iterator<Entry<String, JsonElement>> it = obj.entrySet().iterator();
      while (it.hasNext()) {
        Entry<String, JsonElement> entry = it.next();
        if (entry.getValue().isJsonNull()) {
          it.remove();
        }
      }
    }

    return obj;
  }

  public abstract JsonObject buildJsonObject();


  public String buildJson() {
    return this.buildJsonObject().toString();
  }

  /**
   * Strips all key-value with null values.
   * @return
   */
  public String buildSendableJson() {
    return this.buildSendableJsonObject(true).toString();
  }
  
  
  
}
