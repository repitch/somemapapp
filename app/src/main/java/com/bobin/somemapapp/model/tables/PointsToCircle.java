package com.bobin.somemapapp.model.tables;

import io.realm.RealmObject;

public class PointsToCircle extends RealmObject {
    public PointsToCircle() {
    }

    public PointsToCircle(String depositionPointExternalId, String circleId) {
        this.depositionPointExternalId = depositionPointExternalId;
        this.circleId = circleId;
    }

    private String depositionPointExternalId;
    private String circleId;

    public String getDepositionPointExternalId() {
        return depositionPointExternalId;
    }

    public void setDepositionPointExternalId(String depositionPointExternalId) {
        this.depositionPointExternalId = depositionPointExternalId;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }
}
