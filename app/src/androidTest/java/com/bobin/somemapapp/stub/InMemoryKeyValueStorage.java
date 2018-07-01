package com.bobin.somemapapp.stub;

import com.bobin.somemapapp.storage.KeyValueStorage;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class InMemoryKeyValueStorage implements KeyValueStorage {
    private HashMap<String, Object> hash;

    public InMemoryKeyValueStorage() {
        this.hash = new HashMap<>();
    }

    @Override
    public void save(String key, Serializable value) {
        hash.put(key, value);
    }

    @Override
    public void save(String key, long value) {
        hash.put(key, value);
    }

    @Override
    public long getLong(String key) {
        return (long) hash.get(key);
    }

    @Override
    public <T extends Serializable> T getSerializable(String key, Class<T> clazz) {
        return (T) hash.get(key);
    }

    @Override
    public <T extends Serializable> T getSerializable(String key, Class<T> clazz, T defaultValue) {
        if (!hash.containsKey(key))
            return defaultValue;
        return (T) hash.get(key);
    }
}
