package com.bobin.somemapapp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.bobin.somemapapp.MapApp;
import com.bobin.somemapapp.infrastructure.PartnersService;
import com.bobin.somemapapp.infrastructure.PointWatchedService;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.ui.adapter.DepositionPointsListAdapter;
import com.bobin.somemapapp.ui.view.DepositionPointsListView;
import com.bobin.somemapapp.utils.CollectionUtils;
import com.bobin.somemapapp.utils.GoogleMapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class DepositionPointsListPresenter extends MvpPresenter<DepositionPointsListView> {
    @SuppressWarnings("WeakerAccess")
    @Inject
    PartnersService partnersService;
    @SuppressWarnings("WeakerAccess")
    @Inject
    PointWatchedService watchedService;

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
                    .toList()
                    .blockingGet();
        } else {
            sortedPoints = points;
        }

        Disposable subscribe = getBindingDataAsync(sortedPoints)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                    this.currentData = x;
                    getViewState().updateList(x);
                });

        compositeDisposable.add(subscribe);
    }

    private Single<List<DepositionPointsListAdapter.BindData>> getBindingDataAsync(List<DepositionPoint> points) {
        List<String> partnersIds = CollectionUtils.map(points, DepositionPoint::getPartnerName);

        return partnersService
                .getPartnersByIds(partnersIds)
                .subscribeOn(Schedulers.io())
                .flatMap(x -> Single.just(getBindData(points, x)));
    }

    private List<DepositionPointsListAdapter.BindData> getBindData(List<DepositionPoint> points,
                                                                   HashMap<String, DepositionPartner> partners) {
        List<String> pointsIds = CollectionUtils.map(points, DepositionPoint::getExternalId);
        HashSet<String> watchedSet = watchedService.isWatched(pointsIds);

        List<DepositionPointsListAdapter.BindData> result = new ArrayList<>(points.size());
        for (DepositionPoint point : points) {
            boolean watched = watchedSet.contains(point.getExternalId());
            DepositionPartner partner = partners.get(point.getPartnerName());
            DepositionPointsListAdapter.BindData bindData =
                    new DepositionPointsListAdapter.BindData(point, watched, partner);
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

        HashSet<String> watchedSet = watchedService
                .isWatched(CollectionUtils.map(currentData, x -> x.getPoint().getExternalId()));

        for (int i = 0; i < currentData.size(); ++i) {
            DepositionPointsListAdapter.BindData data = currentData.get(i);
            boolean oldWatched = data.isWatched();
            boolean newWatched = watchedSet.contains(data.getPoint().getExternalId());
            if (oldWatched != newWatched) {
                data.setWatched(newWatched);
                getViewState().updateElement(data, i);
            }
        }
    }
}
