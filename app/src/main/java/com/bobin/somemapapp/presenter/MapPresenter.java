package com.bobin.somemapapp.presenter;

import android.Manifest;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Pair;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.bobin.somemapapp.model.CameraBounds;
import com.bobin.somemapapp.model.response.DepositionPointResponse;
import com.bobin.somemapapp.model.response.TinkoffApiResponse;
import com.bobin.somemapapp.model.tables.PointsCircle;
import com.bobin.somemapapp.network.api.TinkoffApi;
import com.bobin.somemapapp.network.api.TinkoffApiFactory;
import com.bobin.somemapapp.ui.view.MapView;
import com.bobin.somemapapp.utils.GoogleMapUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@InjectViewState
public class MapPresenter extends MvpPresenter<MapView> {
    private CompositeDisposable compositeDisposable;
    private TinkoffApi tinkoffApi;
    private PublishSubject<CameraBounds> cameraBoundsPublishSubject;
    private PointsCircle currentCircle;

    public MapPresenter() {
        compositeDisposable = new CompositeDisposable();
        tinkoffApi = new TinkoffApiFactory().createApi();
        cameraBoundsPublishSubject = PublishSubject.create();
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
                .filter(x -> currentCircle == null || !currentCircle.contains(GoogleMapUtils.toCircle(x)))
                .flatMap(x -> getDepositionPoints(x).subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                    currentCircle = x.circle;
                    getViewState().showPins(x.points);
                });
        compositeDisposable.add(subscribe);
    }

    private Observable<CircleWithPoints> getDepositionPoints(CameraBounds bounds) {
        final PointsCircle pointsCircle = GoogleMapUtils.toCircle(bounds);

        return tinkoffApi.getDepositionPoints(
                pointsCircle.getCenterLatitude(),
                pointsCircle.getCenterLongitude(),
                pointsCircle.getRadius())
                .toObservable()
                .flatMapIterable(TinkoffApiResponse::getPayload)
                .toList()
                .toObservable()
                .map(x -> new CircleWithPoints(pointsCircle, x));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    public void mapCameraStops(CameraBounds bounds) {
        Log.d("MapPresenter", "mapCameraStops");
        cameraBoundsPublishSubject.onNext(bounds);
    }

    private static class CircleWithPoints {
        PointsCircle circle;
        List<DepositionPointResponse> points;

        CircleWithPoints(PointsCircle circle, List<DepositionPointResponse> points) {
            this.circle = circle;
            this.points = points;
        }
    }
}
