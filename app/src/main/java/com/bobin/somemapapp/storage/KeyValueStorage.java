package com.bobin.somemapapp.storage;

public interface KeyValueStorage {
    void save(String key, long value);

    long getLong(String key);
}
