package com.invisiblecollector.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Map<String, Object> getOnlyFields(String ... keys) {
        Map<String, Object> copy = getFields();
        List<String> keyList = Arrays.asList(keys);

        return copy.entrySet()
                .stream()
                .filter(pair -> keyList.contains(pair.getKey()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }
}
