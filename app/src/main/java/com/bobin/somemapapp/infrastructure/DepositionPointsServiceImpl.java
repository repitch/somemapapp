package com.bobin.somemapapp.infrastructure;

import com.bobin.somemapapp.model.response.TinkoffApiResponse;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.model.tables.PointsCircle;
import com.bobin.somemapapp.network.api.TinkoffApi;
import com.bobin.somemapapp.storage.PointsCache;
import com.bobin.somemapapp.utils.StorageUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class DepositionPointsServiceImpl implements DepositionPointsService {
    private TinkoffApi tinkoffApi;
    private PointsCache pointsCache;

    public DepositionPointsServiceImpl(TinkoffApi tinkoffApi, PointsCache pointsCache) {
        this.tinkoffApi = tinkoffApi;
        this.pointsCache = pointsCache;
    }

    @Override
    public Single<List<DepositionPoint>> getPoints(final double latitude,
                                                   final double longitude,
                                                   final int radius) {

        List<DepositionPoint> points = pointsCache.getPointsOrNull(latitude, longitude, radius);

        if (points != null && !points.isEmpty())
            return Single.just(points);

        return tinkoffApi.getDepositionPoints(latitude, longitude, radius)
                .toObservable()
                .map(TinkoffApiResponse::getPayload)
                .flatMapIterable(x -> x)
                .map(StorageUtils::convert)
                .toList()
                .toObservable()
                .onErrorResumeNext(Observable.just(new ArrayList<>(0)))
                .doOnNext(x -> pointsCache.savePoints(latitude, longitude, radius, x))
                .first(new ArrayList<>(0));
    }

    @Override
    public Single<List<DepositionPoint>> getPoints(PointsCircle circle) {
        return getPoints(circle.getCenterLatitude(), circle.getCenterLongitude(), circle.getRadius());
    }
}
