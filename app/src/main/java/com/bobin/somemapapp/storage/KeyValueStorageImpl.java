package com.bobin.somemapapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class KeyValueStorageImpl implements KeyValueStorage {

    private final SharedPreferences prefs;

    public KeyValueStorageImpl(Context context) {
        prefs = context.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public void save(String key, long value) {
        prefs.edit().putLong(key, value).apply();
    }

    @Override
    public long getLong(String key) {
        return prefs.getLong(key, 0L);
    }
}
