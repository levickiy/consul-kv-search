package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class KvEntry {
    @JsonProperty("Key")
    private String key;

    @JsonProperty("Value")
    private String rawValue;

    public String getKey() {
        return key;
    }

    public String getValue() {
        if (rawValue == null) return "";
        try {
            byte[] decoded = Base64.getDecoder().decode(rawValue);
            return new String(decoded, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return "[invalid base64]";
        }
    }
}