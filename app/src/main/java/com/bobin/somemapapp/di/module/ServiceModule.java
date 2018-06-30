package com.bobin.somemapapp.di.module;

import com.bobin.somemapapp.infrastructure.DepositionPointsService;
import com.bobin.somemapapp.infrastructure.DepositionPointsServiceImpl;
import com.bobin.somemapapp.infrastructure.PartnersService;
import com.bobin.somemapapp.infrastructure.PartnersServiceImpl;
import com.bobin.somemapapp.infrastructure.PointWatchedService;
import com.bobin.somemapapp.infrastructure.PointWatchedServiceImpl;
import com.bobin.somemapapp.network.api.TinkoffApi;
import com.bobin.somemapapp.storage.PartnersCache;
import com.bobin.somemapapp.storage.PointsCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {
    @Provides
    @Singleton
    public DepositionPointsService provideDepositionPointsService(TinkoffApi tinkoffApi,
                                                                  PointsCache pointsCache) {
        return new DepositionPointsServiceImpl(tinkoffApi, pointsCache);
    }

    @Provides
    @Singleton
    public PartnersService providePartnersService(TinkoffApi tinkoffApi,
                                                  PartnersCache partnersCache) {
        return new PartnersServiceImpl(tinkoffApi, partnersCache);
    }

    @Provides
    @Singleton
    public PointWatchedService providePointWatchedService() {
        return new PointWatchedServiceImpl();
    }
}
