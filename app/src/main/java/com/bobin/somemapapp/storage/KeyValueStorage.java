package com.bobin.somemapapp.storage;

import java.io.Serializable;

public interface KeyValueStorage {
    void save(String key, Serializable value);

    void save(String key, long value);

    long getLong(String key);

    <T extends Serializable> T getSerializable(String key, Class<T> clazz);

    <T extends Serializable> T getSerializable(String key, Class<T> clazz, T defaultValue);
}
