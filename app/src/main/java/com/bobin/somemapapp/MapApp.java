package com.bobin.somemapapp;

import android.app.Application;

import com.bobin.somemapapp.di.component.ApplicationComponent;
import com.bobin.somemapapp.di.component.DaggerApplicationComponent;
import com.bobin.somemapapp.di.module.ApplicationModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MapApp extends Application {
    private static ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
        initComponent();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    private void initComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
