package com.bobin.somemapapp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.bobin.somemapapp.MapApp;
import com.bobin.somemapapp.infrastructure.PartnersService;
import com.bobin.somemapapp.infrastructure.PartnersServiceImpl;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.network.api.TinkoffApiFactory;
import com.bobin.somemapapp.storage.KeyValueStorageImpl;
import com.bobin.somemapapp.storage.PartnersCacheImpl;
import com.bobin.somemapapp.ui.view.DepositionPointsListView;
import com.bobin.somemapapp.utils.GoogleMapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class DepositionPointsListPresenter extends MvpPresenter<DepositionPointsListView> {
    private PartnersService partnersService;
    private CompositeDisposable compositeDisposable;

    public DepositionPointsListPresenter() {
        partnersService = new PartnersServiceImpl(new TinkoffApiFactory().createApi(), new PartnersCacheImpl(new KeyValueStorageImpl(MapApp.context)));
        compositeDisposable = new CompositeDisposable();
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


        Disposable subscribe = Observable.fromIterable(points)
                .map(DepositionPoint::getPartnerName)
                .distinct()
                .toList()
                .flatMap(x -> partnersService.getPartnersByIds(x).subscribeOn(Schedulers.io()))
                .toObservable()
                .flatMapIterable(x -> x)
                .collect((Callable<HashMap<String, String>>) HashMap::new, (m, p) -> m.put(p.getId(), p.getFullPictureUrl()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(m -> getViewState().updateList(sortedPoints, m));
        compositeDisposable.add(subscribe);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
