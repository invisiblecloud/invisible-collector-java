package com.invisiblecollector.model.builder;

import com.invisiblecollector.model.FindDebtsBuilder;
import com.invisiblecollector.model.Model;

import java.util.HashMap;
import java.util.Map;

public class FindDebtsBuilderBuilder extends BuilderBase {

  private class ExtraFindDebtsBuilder extends FindDebtsBuilder {
    public ExtraFindDebtsBuilder(Map<String, Object> fields) {
      this.fields = new HashMap<>(fields);
    }
  }

  protected Map<String, Object> fields = new HashMap<>();

  private FindDebtsBuilderBuilder(Map<String, Object> fields) {
    this.fields = fields;
  }

  public static FindDebtsBuilderBuilder buildTestBuilder() {
    Map<String, Object> fields = new HashMap<>();

    fields.put("from_date", "2010-01-01");
    fields.put("to_date", "2011-01-01");
    fields.put("from_duedate", "2012-01-01");
    fields.put("number", "123");

    return new FindDebtsBuilderBuilder(fields);
  }

  @Override
  public FindDebtsBuilder buildModel() {
    return new ExtraFindDebtsBuilder(fields);
  }

  @Override
  protected Map<String, Object> buildSendableObject() {
    return buildObject();
  }

  @Override
  public Map<String, Object> buildObject() {
    return new HashMap<>(fields);
  }
}
