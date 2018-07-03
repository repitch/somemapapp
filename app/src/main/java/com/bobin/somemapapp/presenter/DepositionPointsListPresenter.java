package com.bobin.somemapapp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.bobin.somemapapp.MapApp;
import com.bobin.somemapapp.infrastructure.ExceptionsHandler;
import com.bobin.somemapapp.infrastructure.PartnersService;
import com.bobin.somemapapp.infrastructure.PointWatchedService;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.ScreenDensityUrlCalculator;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.ui.adapter.DepositionPointsListAdapter;
import com.bobin.somemapapp.ui.view.DepositionPointsListView;
import com.bobin.somemapapp.utils.CollectionUtils;
import com.bobin.somemapapp.utils.GoogleMapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class DepositionPointsListPresenter extends MvpPresenter<DepositionPointsListView> {
    @Inject
    PartnersService partnersService;
    @Inject
    PointWatchedService watchedService;
    @Inject
    ScreenDensityUrlCalculator urlCalculator;
    @Inject
    ExceptionsHandler exceptionsHandler;

    private CompositeDisposable compositeDisposable;
    private List<DepositionPointsListAdapter.BindData> currentData;

    public DepositionPointsListPresenter() {
        compositeDisposable = new CompositeDisposable();
        MapApp.getComponent().inject(this);
    }

    public void updateList(List<DepositionPoint> points, MapCoordinates userLocation) {
        final List<DepositionPoint> sortedPoints;

        if (userLocation != null) {
            sortedPoints = Observable.fromIterable(points)
                    .sorted((x, y) -> {
                        float dx = GoogleMapUtils.distanceBetween(x.getMapCoordinates(), userLocation);
                        float dy = GoogleMapUtils.distanceBetween(y.getMapCoordinates(), userLocation);
                        return Float.compare(dx, dy);
                    })
                    .toList() // toSortedList сразу
                    .blockingGet(); // blocking get плохо
        } else {
            sortedPoints = points;
        }

        Disposable subscribe = getBindingDataAsync(sortedPoints)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        x -> {
                            this.currentData = x;
                            getViewState().updateList(x);
                        },
                        this::handleThrowable);

        compositeDisposable.add(subscribe);
    }

    private void handleThrowable(Throwable throwable) {
        String message = exceptionsHandler.getSnackbarMessage(throwable);
        if (message != null)
            getViewState().showSnackbar(message);
    }

    private Single<List<DepositionPointsListAdapter.BindData>> getBindingDataAsync(List<DepositionPoint> points) {
        List<String> partnersIds = CollectionUtils.map(points, DepositionPoint::getPartnerName); // это можно делать в цепочке rx

        return partnersService
                .getPartnersByIds(partnersIds)
                .subscribeOn(Schedulers.io())
                .flatMap(x -> Single.just(getBindData(points, x)));
    }

    private List<DepositionPointsListAdapter.BindData> getBindData(List<DepositionPoint> points,
                                                                   HashMap<String, DepositionPartner> partners) {
        List<DepositionPointsListAdapter.BindData> result = new ArrayList<>(points.size());
        for (DepositionPoint point : points) {
            DepositionPartner partner = partners.get(point.getPartnerName());
            DepositionPointsListAdapter.BindData bindData =
                    new DepositionPointsListAdapter.BindData(point,
                            watchedService.isWatched(point.getExternalId()),
                            partner.getName(),
                            urlCalculator.getPartnerPictureUrl(partner));
            result.add(bindData);
        }
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    public void onResume() {
        if (currentData == null)
            return;

        for (int i = 0; i < currentData.size(); ++i) {
            DepositionPointsListAdapter.BindData data = currentData.get(i);
            if (watchedService.isWatched(data.getPoint().getExternalId())) {
                data.setWatched(true);
                getViewState().updateElement(data, i);
            }
        }
    }
}
