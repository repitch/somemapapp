package com.bobin.somemapapp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.bobin.somemapapp.MapApp;
import com.bobin.somemapapp.infrastructure.PartnersService;
import com.bobin.somemapapp.infrastructure.PartnersServiceImpl;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.network.api.TinkoffApiFactory;
import com.bobin.somemapapp.storage.KeyValueStorageImpl;
import com.bobin.somemapapp.storage.PartnersCacheImpl;
import com.bobin.somemapapp.ui.view.DepositionPointDetailView;
import com.bobin.somemapapp.utils.GoogleMapUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class DepositionPointDetailPresenter extends MvpPresenter<DepositionPointDetailView> {
    private PartnersService partnersService;
    private CompositeDisposable compositeDisposable;

    public DepositionPointDetailPresenter() {
        partnersService = new PartnersServiceImpl(new TinkoffApiFactory().createApi(), new PartnersCacheImpl(new KeyValueStorageImpl(MapApp.context)));
        compositeDisposable = new CompositeDisposable();
    }

    public void onStart(String partnerId, MapCoordinates pointLocation, MapCoordinates userPosition) {
        if (userPosition != null) {
            int meters = (int) GoogleMapUtils.distanceBetween(pointLocation, userPosition);
            getViewState().showDistance(meters);
        }

        Disposable subscribe = partnersService.getPartnerById(partnerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(partner -> getViewState().showPartner(partner),
                        t -> getViewState().finishActivity());
        compositeDisposable.add(subscribe);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
