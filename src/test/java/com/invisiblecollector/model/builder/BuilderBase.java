package com.invisiblecollector.model.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.invisiblecollector.model.Model;
import com.invisiblecollector.model.json.JsonSingleton;

import java.util.Map;

public abstract class BuilderBase {

  public abstract Model buildModel();
  
  protected abstract Map<String, Object> buildSendableObject();

  private Map<String, Object> buildSendableObject(boolean stripNulls) {
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


  public String buildJson() {
    try {
      return JsonSingleton.getInstance2().writeValueAsString(this.buildObject());
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Strips all key-value with null values.
   * @return
   * @param stripNulls
   */
  public String buildSendableJson(boolean stripNulls) {
    try {
      return JsonSingleton.getInstance2().writeValueAsString(this.buildSendableObject(stripNulls));
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }


  
  
  
}
