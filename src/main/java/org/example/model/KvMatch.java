package org.example.model;

public class KvMatch {
    private final String key;
    private final String value;

    public KvMatch(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}