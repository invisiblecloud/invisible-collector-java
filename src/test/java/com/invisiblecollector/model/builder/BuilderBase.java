package com.invisiblecollector.model.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.invisiblecollector.model.Model;
import com.invisiblecollector.model.json.JsonSingleton;

import java.util.Map;

public abstract class BuilderBase {

  public abstract Model buildModel();

  protected abstract Map<String, Object> buildSendableObject();

  protected Map<String, Object> buildSendableObject(boolean stripNulls) {
    Map<String, Object> obj = buildSendableObject();

    if (stripNulls) {
      stripMapNulls(obj);
    }

    return obj;
  }

  protected void stripMapNulls(Map<String, Object> obj) {
    obj.entrySet().removeIf(entry -> entry.getValue() == null);
  }

  public abstract Map<String, Object> buildObject();

  protected static String toJson(Object obj) {
    try {
      return JsonSingleton.getInstance().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }

  public String buildJson() {
    return toJson(this.buildObject());
  }

  /**
   * Strips all key-value with null values.
   *
   * @return
   * @param stripNulls
   */
  public String buildSendableJson(boolean stripNulls) {
    return toJson(this.buildSendableObject(stripNulls));
  }
}
