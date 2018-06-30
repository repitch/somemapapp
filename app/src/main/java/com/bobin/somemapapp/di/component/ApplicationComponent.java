package com.bobin.somemapapp.di.component;

import com.bobin.somemapapp.di.module.ApiModule;
import com.bobin.somemapapp.di.module.ApplicationModule;
import com.bobin.somemapapp.di.module.ServiceModule;
import com.bobin.somemapapp.di.module.StorageModule;
import com.bobin.somemapapp.presenter.DepositionPointDetailPresenter;
import com.bobin.somemapapp.presenter.DepositionPointsListPresenter;
import com.bobin.somemapapp.presenter.MapPresenter;
import com.bobin.somemapapp.ui.fragment.PointDetailBottomSheet;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class,
        ServiceModule.class, StorageModule.class})
public interface ApplicationComponent {

    void inject(PointDetailBottomSheet pointDetailBottomSheet);

    void inject(DepositionPointDetailPresenter depositionPointDetailPresenter);

    void inject(DepositionPointsListPresenter depositionPointsListPresenter);

    void inject(MapPresenter mapPresenter);
}
