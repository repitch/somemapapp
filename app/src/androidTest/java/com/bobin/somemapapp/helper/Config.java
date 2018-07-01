package com.bobin.somemapapp.helper;

import android.support.test.InstrumentationRegistry;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Config {
    public static void initRealm() {
        Realm.init(InstrumentationRegistry.getTargetContext());
        Realm.setDefaultConfiguration(getConfig());
    }

    private static RealmConfiguration getConfig() {
        return new RealmConfiguration.Builder()
                .inMemory()
                .name("test-realm" + DataGenerator.randomString())
                .build();
    }
}
