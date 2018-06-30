package com.bobin.somemapapp.presenter;

import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.bobin.somemapapp.MapApp;
import com.bobin.somemapapp.infrastructure.PartnersService;
import com.bobin.somemapapp.infrastructure.PointWatchedService;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.model.tables.Limit;
import com.bobin.somemapapp.ui.view.DepositionPointDetailView;
import com.bobin.somemapapp.utils.GoogleMapUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class DepositionPointDetailPresenter extends MvpPresenter<DepositionPointDetailView> {
    private CompositeDisposable compositeDisposable;
    private DepositionPartner partner;

    @SuppressWarnings("WeakerAccess")
    @Inject
    PartnersService partnersService;
    @SuppressWarnings("WeakerAccess")
    @Inject
    PointWatchedService watchedService;

    public DepositionPointDetailPresenter() {
        compositeDisposable = new CompositeDisposable();
        MapApp.getComponent().inject(this);
    }

    public Limit getLimit(int id) {
        return partner.getLimits().get(id);
    }

    public void onStart(String partnerId, MapCoordinates pointLocation, MapCoordinates userPosition) {
        if (userPosition != null) {
            int meters = (int) GoogleMapUtils.distanceBetween(pointLocation, userPosition);
            getViewState().showDistance(meters);
        }

        Disposable subscribe = partnersService.getPartnerById(partnerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        partner -> {
                            this.partner = partner;
                            getViewState().showPartner(partner);
                        },
                        t -> getViewState().finishActivity());
        compositeDisposable.add(subscribe);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    public void goToWebSite() {
        if (partner != null) {
            String url = partner.getUrl();
            getViewState().openBrowser(Uri.parse(url));
        }
    }

    public void setPointWatched(String pointId) {
        watchedService.setWatched(pointId);
    }
}
