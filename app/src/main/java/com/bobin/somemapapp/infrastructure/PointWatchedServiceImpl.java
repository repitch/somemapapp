package com.bobin.somemapapp.infrastructure;

import com.bobin.somemapapp.model.tables.PointWatchState;

import java.util.HashMap;

import io.realm.Realm;

public class PointWatchedServiceImpl implements PointWatchedService {
    private HashMap<String, Boolean> localCache;

    public PointWatchedServiceImpl() {
        localCache = new HashMap<>();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isWatched(String pointId) {
        if (localCache.containsKey(pointId) && localCache.get(pointId))
            return true;

        Realm realm = Realm.getDefaultInstance(); // лучше провайдить через Dagger
        long statesCount = realm.where(PointWatchState.class)
                .equalTo("pointId", pointId)
                .count();

        boolean result = statesCount > 0;
        realm.close();

        localCache.put(pointId, result);
        return result;
    }

    @Override
    public void setWatched(String pointId) {
        localCache.put(pointId, true);
        PointWatchState pointWatchState = new PointWatchState();
        pointWatchState.setPointId(pointId);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> r.insertOrUpdate(pointWatchState));
        realm.close();
    }
}
