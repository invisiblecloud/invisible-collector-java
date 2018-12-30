package com.invisiblecollector.model.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.invisiblecollector.model.Model;
import com.invisiblecollector.model.json.JsonSingleton;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public abstract class BuilderBase {

  public abstract Model buildModel();
  
  protected abstract Map<String, Object> buildSendableObject();

  public Map<String, Object> buildSendableObject(boolean stripNulls) {
    Map<String, Object> obj = buildSendableObject();

    if (stripNulls) {
      obj.entrySet().removeIf(entry -> entry.getValue() == null);
    }

    return obj;
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
   */
  public String buildSendableJson() {
    try {
      return JsonSingleton.getInstance2().writeValueAsString(this.buildSendableObject(true));
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }
  
  
  
}
