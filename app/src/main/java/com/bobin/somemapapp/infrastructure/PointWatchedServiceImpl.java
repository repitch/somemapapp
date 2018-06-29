package com.bobin.somemapapp.infrastructure;

import com.bobin.somemapapp.model.tables.PointWatchState;

import java.util.HashSet;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class PointWatchedServiceImpl implements PointWatchedService {
    @Override
    public HashSet<String> isWatched(List<String> ids) {
        String[] idsArray = new String[ids.size()];
        for (int i = 0; i < ids.size(); ++i)
            idsArray[i] = ids.get(i);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<PointWatchState> states = realm.where(PointWatchState.class)
                .in("pointId", idsArray)
                .findAll();

        HashSet<String> result = new HashSet<>();

        for (PointWatchState state : states) {
            result.add(state.getPointId());
        }

        return result;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isWatched(String pointId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PointWatchState> states = realm.where(PointWatchState.class)
                .equalTo("pointId", pointId)
                .findAll();
        return states.size() != 0;
    }

    @Override
    public void setWatched(String pointId) {
        PointWatchState pointWatchState = new PointWatchState();
        pointWatchState.setPointId(pointId);
        Realm.getDefaultInstance().executeTransaction(r -> r.insertOrUpdate(pointWatchState));
    }
}
