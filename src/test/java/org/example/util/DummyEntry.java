package org.example.util;

import org.example.model.KvEntry;

// Дозволяємо створювати "дитячий" клас, щоб задати getKey/getValue
class DummyEntry extends KvEntry {
    private final String key;
    private final String value;

    public DummyEntry(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
