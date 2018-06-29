package com.bobin.somemapapp.infrastructure;

import java.util.HashSet;
import java.util.List;

public interface PointWatchedService {
    HashSet<String> isWatched(List<String> ids);

    boolean isWatched(String pointId);

    void setWatched(String pointId);
}
