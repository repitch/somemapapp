package com.bobin.somemapapp.di.module;

import android.content.Context;

import com.bobin.somemapapp.infrastructure.Clock;
import com.bobin.somemapapp.storage.KeyValueStorage;
import com.bobin.somemapapp.storage.KeyValueStorageImpl;
import com.bobin.somemapapp.storage.PartnersCache;
import com.bobin.somemapapp.storage.PartnersCacheImpl;
import com.bobin.somemapapp.storage.PointsCache;
import com.bobin.somemapapp.storage.PointsCacheImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {
    @Provides
    @Singleton
    public KeyValueStorage provideKeyValueStorage(Context context) {
        return new KeyValueStorageImpl(context);
    }

    @Provides
    @Singleton
    public PartnersCache providePartnersCache(KeyValueStorage keyValueStorage, Clock clock) {
        return new PartnersCacheImpl(keyValueStorage, clock);
    }

    @Provides
    @Singleton
    public PointsCache providePointsCache(Clock clock) {
        return new PointsCacheImpl(clock);
    }
}
