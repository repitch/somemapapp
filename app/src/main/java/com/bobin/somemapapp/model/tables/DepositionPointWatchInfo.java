package com.bobin.somemapapp.model.tables;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DepositionPointWatchInfo extends RealmObject {
    @PrimaryKey
    private String pointExternalId;
    private boolean watched;

    public String getPointExternalId() {
        return pointExternalId;
    }

    public void setPointExternalId(String pointExternalId) {
        this.pointExternalId = pointExternalId;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}
