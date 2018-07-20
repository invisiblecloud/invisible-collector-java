package com.ic.invoicecapture.model.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ic.invoicecapture.model.IModel;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class BuilderBase {
  public abstract IModel buildModel();

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

  public String buildSendableJson() {
    return this.buildSendableJsonObject(true).toString();
  }
}
