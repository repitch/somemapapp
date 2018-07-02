package com.bobin.somemapapp.infrastructure;

public interface PointWatchedService {
    boolean isWatched(String pointId);

    void setWatched(String pointId);
}
