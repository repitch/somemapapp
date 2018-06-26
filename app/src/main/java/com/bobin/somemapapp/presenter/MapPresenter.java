package com.bobin.somemapapp.presenter;

import android.Manifest;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.bobin.somemapapp.MapApp;
import com.bobin.somemapapp.infrastructure.DepositionPointsService;
import com.bobin.somemapapp.infrastructure.DepositionPointsServiceImpl;
import com.bobin.somemapapp.infrastructure.PartnersService;
import com.bobin.somemapapp.infrastructure.PartnersServiceImpl;
import com.bobin.somemapapp.model.CameraBounds;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.model.tables.PointsCircle;
import com.bobin.somemapapp.network.api.TinkoffApi;
import com.bobin.somemapapp.network.api.TinkoffApiFactory;
import com.bobin.somemapapp.storage.KeyValueStorageImpl;
import com.bobin.somemapapp.storage.PartnersCacheImpl;
import com.bobin.somemapapp.storage.PointsCacheImpl;
import com.bobin.somemapapp.ui.view.MapView;
import com.bobin.somemapapp.utils.GoogleMapUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@InjectViewState
public class MapPresenter extends MvpPresenter<MapView> {
    private CompositeDisposable compositeDisposable;
    private PublishSubject<CameraBounds> cameraBoundsPublishSubject;
    private DepositionPointsService depositionPointsService;
    private CircleWithPoints currentScreenData;
    private PartnersService partnersService;

    public MapPresenter() {
        TinkoffApi api = new TinkoffApiFactory().createApi();
        partnersService = new PartnersServiceImpl(api, new PartnersCacheImpl(new KeyValueStorageImpl(MapApp.context)));
        compositeDisposable = new CompositeDisposable();
        cameraBoundsPublishSubject = PublishSubject.create();
        depositionPointsService = new DepositionPointsServiceImpl(api, new PointsCacheImpl());
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
                .onErrorResumeNext(Observable.empty())
                .subscribe(x -> {
                    getViewState().showPins(x.points);
                    currentScreenData = x;
                });
        compositeDisposable.add(subscribe);
    }

    // удалять штуки, которые вне экрана - из-за них лагает
    private boolean needLoadPoints(CameraBounds bounds) {
        if (currentScreenData == null) {
            Log.d("MapPresenter", "currentScreenData == null");
            return true;
        }

        PointsCircle circle = GoogleMapUtils.toCircle(bounds);

        List<DepositionPoint> pointsFromCircle =
                GoogleMapUtils.pointsFromCircle(circle, currentScreenData.points);

        if (!currentScreenData.circle.contains(circle)) {
            Log.d("MapPresenter", "!currentScreenData.circle.contains(circle)");
            return true;
        }
        if (pointsFromCircle.size() == 0) {
            Log.d("MapPresenter", "pointsFromCircle.size() == 0");
            return true;
        }
        Log.d("MapPresenter", "filtered");
        return false;
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

    public void clickOnMarker(double latitude, double longitude) {
        DepositionPoint point = findPoint(latitude, longitude);
        if (point != null) {
            Disposable subscribe = partnersService.getPartnerById(point.getPartnerName())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(partner -> {
                        String imgUrl = "https://static.tinkoff.ru/icons/deposition-partners-v3/mdpi/" + partner.getPicture();
                        getViewState().showBottomSheet(point, imgUrl);
                    });
            compositeDisposable.add(subscribe);
        }

    }

    public void mapCameraStops(CameraBounds bounds) {
        cameraBoundsPublishSubject.onNext(bounds);
    }

    private DepositionPoint findPoint(double latitude, double longitude) {
        if (currentScreenData == null)
            return null;
        for (DepositionPoint point : currentScreenData.points) {
            if (point.getLatitude() == latitude && point.getLongitude() == longitude)
                return point;
        }
        return null;
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
