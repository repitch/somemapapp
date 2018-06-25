package com.bobin.somemapapp.infrastructure;

import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.model.tables.PointsCircle;

import java.util.List;

import io.reactivex.Single;

public interface DepositionPointsService {
    Single<List<DepositionPoint>> getPoints(double latitude,
                                            double longitude,
                                            int radius);

    Single<List<DepositionPoint>> getPoints(PointsCircle circle);
}
