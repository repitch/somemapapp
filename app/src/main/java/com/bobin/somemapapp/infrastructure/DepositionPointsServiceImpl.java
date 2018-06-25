package com.bobin.somemapapp.infrastructure;

import com.bobin.somemapapp.model.response.TinkoffApiResponse;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.model.tables.PointsCircle;
import com.bobin.somemapapp.network.api.TinkoffApi;
import com.bobin.somemapapp.storage.PointsCache;
import com.bobin.somemapapp.utils.GoogleMapUtils;
import com.bobin.somemapapp.utils.StorageUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

class DepositionPointsServiceImpl implements DepositionPointsService {
    private TinkoffApi tinkoffApi;
    private PointsCache pointsCache;

    public DepositionPointsServiceImpl(TinkoffApi tinkoffApi, PointsCache pointsCache) {
        this.tinkoffApi = tinkoffApi;
        this.pointsCache = pointsCache;
    }

    @Override
    public Single<List<DepositionPoint>> getPoints(double latitude,
                                                   double longitude,
                                                   int radius) {

        PointsCircle pointsCircle = new PointsCircle(latitude, longitude, radius);

        List<DepositionPoint> points = pointsCache.getPointsOrNull(latitude, longitude, radius);

        if (points != null)
            return Single.just(points);

        tinkoffApi.getDepositionPoints(latitude, longitude, radius)
                .map(TinkoffApiResponse::getPayload)
                .toObservable()
                .flatMapIterable(x -> x)
                .map(x -> StorageUtils.convert(x))
                .toList()
                .toObservable()
                .doOnNext()



    }
}
