package com.bobin.somemapapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MapApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initRealm();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
