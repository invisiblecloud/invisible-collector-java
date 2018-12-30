package com.invisiblecollector.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Model {
    protected Map<String, Object> fields = new HashMap<>();

    protected String getString(String key) {
        return (String) fields.get(key);
    }

    protected Boolean getBoolean(String key) {
        return (Boolean) fields.get(key);
    }

    public Map<String, Object> getFields() {
        return new HashMap<>(fields);
    }

    public void assertConstainsKeys(String... keys) {
        Arrays.stream(keys)
                .filter(key -> !fields.containsKey(key))
                .forEach(key -> {
                    String msg = String.format("Field %s is missing", key);
                    throw new IllegalArgumentException(msg);
                });
    }
}
