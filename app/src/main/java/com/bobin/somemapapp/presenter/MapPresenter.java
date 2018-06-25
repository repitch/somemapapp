package com.bobin.somemapapp.presenter;

import android.Manifest;
import android.support.v4.app.FragmentActivity;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.bobin.somemapapp.infrastructure.DepositionPointsService;
import com.bobin.somemapapp.infrastructure.DepositionPointsServiceImpl;
import com.bobin.somemapapp.model.CameraBounds;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.model.tables.PointsCircle;
import com.bobin.somemapapp.network.api.TinkoffApiFactory;
import com.bobin.somemapapp.storage.PointsCacheImpl;
import com.bobin.somemapapp.ui.view.MapView;
import com.bobin.somemapapp.utils.GoogleMapUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@InjectViewState
public class MapPresenter extends MvpPresenter<MapView> {
    private final PointsCacheImpl pointsCache;
    private CompositeDisposable compositeDisposable;
    private PublishSubject<CameraBounds> cameraBoundsPublishSubject;
    private DepositionPointsService depositionPointsService;
    private CircleWithPoints currentScreenData;

    public MapPresenter() {
        compositeDisposable = new CompositeDisposable();
        cameraBoundsPublishSubject = PublishSubject.create();
        pointsCache = new PointsCacheImpl();
        depositionPointsService = new DepositionPointsServiceImpl(new TinkoffApiFactory().createApi(),
                pointsCache);
    }

    public void mapIsReady(FragmentActivity activity) {
        if (activity == null)
            return;

        Disposable subscribe = new RxPermissions(activity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(b -> getViewState().setMyLocationButtonEnabled(b));
        compositeDisposable.add(subscribe);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Disposable subscribe = cameraBoundsPublishSubject
                .debounce(1L, TimeUnit.SECONDS)
                .filter(this::needLoadPoints)
                .flatMap(x -> getDepositionPoints(x).observeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                    getViewState().showPins(x.points);
                    currentScreenData = x;
                }, t -> {
                });
        compositeDisposable.add(subscribe);
    }

    private boolean needLoadPoints(CameraBounds bounds) {
        if (currentScreenData == null)
            return true;

        PointsCircle circle = GoogleMapUtils.toCircle(bounds);

        List<DepositionPoint> pointsFromCircle =
                GoogleMapUtils.pointsFromCircle(circle, currentScreenData.points);

        return !currentScreenData.circle.contains(circle) || pointsFromCircle.size() == 0;
    }

    private Observable<CircleWithPoints> getDepositionPoints(CameraBounds bounds) {
        final PointsCircle pointsCircle = GoogleMapUtils.toCircle(bounds);

        return depositionPointsService.getPoints(pointsCircle)
                .map(x -> new CircleWithPoints(pointsCircle, x))
                .toObservable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    public void mapCameraStops(CameraBounds bounds) {
        cameraBoundsPublishSubject.onNext(bounds);
    }

    private static class CircleWithPoints {
        PointsCircle circle;
        List<DepositionPoint> points;

        CircleWithPoints(PointsCircle circle, List<DepositionPoint> points) {
            this.circle = circle;
            this.points = points;
        }
    }
}
