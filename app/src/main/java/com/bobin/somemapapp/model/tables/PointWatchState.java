package com.bobin.somemapapp.model.tables;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PointWatchState extends RealmObject {
    @PrimaryKey
    private String pointId;

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }
}
