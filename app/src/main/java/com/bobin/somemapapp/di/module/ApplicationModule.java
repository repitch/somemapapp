package com.bobin.somemapapp.di.module;

import android.app.Application;
import android.content.Context;

import com.bobin.somemapapp.model.ScreenDensityUrlCalculator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    public ScreenDensityUrlCalculator provideScreenDensityUrlCalculator(Context context) {
        return new ScreenDensityUrlCalculator(context);
    }
}
