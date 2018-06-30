package com.bobin.somemapapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.Serializable;

public class KeyValueStorageImpl implements KeyValueStorage {

    private final SharedPreferences prefs;
    private final Gson gson;

    public KeyValueStorageImpl(Context context) {
        prefs = context.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    @Override
    public void save(String key, Serializable value) {
        String json = gson.toJson(value);
        prefs.edit().putString(key, json).apply();
    }

    @Override
    public void save(String key, long value) {
        prefs.edit().putLong(key, value).apply();
    }

    @Override
    public long getLong(String key) {
        return prefs.getLong(key, 0L);
    }

    @Override
    public <T extends Serializable> T getSerializable(String key, Class<T> clazz) {
        return getSerializable(key, clazz, null);
    }

    @Override
    public <T extends Serializable> T getSerializable(String key, Class<T> clazz, T defaultValue) {
        if (!prefs.contains(key))
            return defaultValue;

        String json = prefs.getString(key, "{}");
        return gson.fromJson(json, clazz);
    }
}
