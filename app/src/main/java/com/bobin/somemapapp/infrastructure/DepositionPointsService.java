package com.bobin.somemapapp.infrastructure;

import com.bobin.somemapapp.model.tables.DepositionPoint;

import java.util.List;

import io.reactivex.Single;

public interface DepositionPointsService {
    Single<List<DepositionPoint>> getPoints(double latitude,
                                            double longitude,
                                            int radius);
}
