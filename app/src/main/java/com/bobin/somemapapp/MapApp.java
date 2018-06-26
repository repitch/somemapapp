package com.bobin.somemapapp;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MapApp extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        initRealm();
        context = getApplicationContext();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
