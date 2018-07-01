package com.bobin.somemapapp.stub;

import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.storage.PointsCache;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class PointsCacheStub implements PointsCache {
    private HashMap<String, List<DepositionPoint>> hash;
    private boolean expired;

    public PointsCacheStub() {
        hash = new HashMap<>();
    }

    @Override
    public void savePoints(double latitude, double longitude, int radius, List<DepositionPoint> points) {
        hash.put(getString(latitude, longitude, radius), points);
    }

    @Nullable
    @Override
    public List<DepositionPoint> getPointsOrNull(double latitude, double longitude, int radius) {
        String key = getString(latitude, longitude, radius);
        if (expired || !hash.containsKey(key))
            return null;
        return hash.get(key);
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    private String getString(double latitude, double longitude, int radius) {
        return latitude + "_" + longitude + "_" + radius;
    }
}
