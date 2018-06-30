package com.bobin.somemapapp.presenter;

import android.Manifest;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.bobin.somemapapp.MapApp;
import com.bobin.somemapapp.infrastructure.DepositionPointsService;
import com.bobin.somemapapp.infrastructure.PartnersService;
import com.bobin.somemapapp.model.CameraBounds;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.ScreenDensityUrlCalculator;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.model.tables.PointsCircle;
import com.bobin.somemapapp.network.NetworkAvailability;
import com.bobin.somemapapp.storage.KeyValueStorage;
import com.bobin.somemapapp.ui.view.MapView;
import com.bobin.somemapapp.utils.GoogleMapUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class MapPresenter extends MvpPresenter<MapView> {
    private static final String LAST_SCREEN_POSITION_KEY = "lastScreenPosition";

    private CompositeDisposable compositeDisposable;
    private PublishSubject<CameraBounds> cameraBoundsPublishSubject;
    private BoundsWithPoints currentScreenData;

    @Inject
    DepositionPointsService depositionPointsService;
    @Inject
    PartnersService partnersService;
    @Inject
    KeyValueStorage keyValueStorage;
    @Inject
    NetworkAvailability networkAvailability;
    @Inject
    ScreenDensityUrlCalculator urlCalculator;

    public MapPresenter() {
        compositeDisposable = new CompositeDisposable();
        cameraBoundsPublishSubject = PublishSubject.create();
        MapApp.getComponent().inject(this);
    }

    public void mapIsReady(FragmentActivity activity) {
        if (activity == null)
            return;

        Disposable subscribe = new RxPermissions(activity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(hasPermission -> {
                    getViewState().setMyLocationButtonEnabled(hasPermission);
                    moveToLastPosition();
                });
        compositeDisposable.add(subscribe);
    }

    private void moveToLastPosition() {
        MapCoordinates targetScreenLocation = keyValueStorage.getSerializable(
                LAST_SCREEN_POSITION_KEY,
                MapCoordinates.class,
                new MapCoordinates(55.751244, 37.618423) // Moscow
        );
        getViewState().moveToPoint(targetScreenLocation);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Disposable subscribe = cameraBoundsPublishSubject
                .debounce(1L, TimeUnit.SECONDS)
                .flatMap(x -> {
                    if (needLoadPoints(x))
                        return getDepositionPoints(x).observeOn(Schedulers.io());
                    currentScreenData.cameraBounds = x;
                    return Observable.just(currentScreenData);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                    getViewState().showPins(x.pointsFromBounds());
                    currentScreenData = x;
                });
        compositeDisposable.add(subscribe);
    }

    private boolean needLoadPoints(CameraBounds bounds) {
        if (currentScreenData == null) {
            Log.d("MapPresenter", "currentScreenData == null");
            return true;
        }

        List<DepositionPoint> pointsFromCircle =
                GoogleMapUtils.pointsFromCameraBounds(bounds, currentScreenData.points);

        if (!GoogleMapUtils.boundsInCircle(currentScreenData.getCircle(), bounds)) {
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

    private Observable<BoundsWithPoints> getDepositionPoints(CameraBounds bounds) {
        final PointsCircle pointsCircle = GoogleMapUtils.toCircle(bounds);

        return depositionPointsService.getPoints(pointsCircle)
                .map(x -> new BoundsWithPoints(x, bounds))
                .toObservable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    public void clickOnMarker(double latitude, double longitude) {
        final DepositionPoint point = findPoint(latitude, longitude);
        if (point != null) {
            Disposable subscribe = partnersService.getPartnerById(point.getPartnerName())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(partner -> getViewState().showBottomSheet(point, partner.getName(), urlCalculator.getPartnerPictureUrl(partner)));
            compositeDisposable.add(subscribe);
        }
    }

    public void mapCameraStops(CameraBounds bounds) {
        cameraBoundsPublishSubject.onNext(bounds);
        MapCoordinates mapCoordinates = new MapCoordinates(bounds.getCenter().getLatitude(),
                bounds.getCenter().getLongitude());
        keyValueStorage.save(LAST_SCREEN_POSITION_KEY, mapCoordinates);
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

    private static class BoundsWithPoints {
        CameraBounds cameraBounds;
        List<DepositionPoint> points;

        BoundsWithPoints(List<DepositionPoint> points,
                         CameraBounds cameraBounds) {
            this.points = points;
            this.cameraBounds = cameraBounds;
        }

        PointsCircle getCircle() {
            return GoogleMapUtils.toCircle(cameraBounds);
        }

        List<DepositionPoint> pointsFromBounds() {
            return GoogleMapUtils.pointsFromCameraBounds(cameraBounds, points);
        }
    }
}
