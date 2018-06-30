package com.bobin.somemapapp.di.module;

import com.bobin.somemapapp.network.api.TinkoffApi;
import com.bobin.somemapapp.network.api.TinkoffApiFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {
    @Provides
    @Singleton
    public TinkoffApi provideTinkoffApi() {
        return TinkoffApiFactory.createApi();
    }
}
