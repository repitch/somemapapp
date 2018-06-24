package com.bobin.somemapapp.storage;

import com.bobin.somemapapp.model.tables.DepositionPoint;

import java.util.List;

import javax.annotation.Nullable;

public interface PointsCache {
    void savePoints(double latitude,
                    double longitude,
                    int radius,
                    List<DepositionPoint> points);

    @Nullable
    List<DepositionPoint> getPointsOrNull(double latitude,
                                          double longitude,
                                          int radius);
}
