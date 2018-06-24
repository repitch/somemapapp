package com.bobin.somemapapp.model.tables;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PointsCircle extends RealmObject {
    public PointsCircle(double centerLatitude, double centerLongitude, int radius) {
        this();
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.radius = radius;
    }

    public PointsCircle() {
        id = UUID.randomUUID().toString();
        timestamp = System.currentTimeMillis();
    }

    @PrimaryKey
    private String id;
    private long timestamp;
    private double centerLatitude;
    private double centerLongitude;
    private int radius;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public double getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getDiameter() {
        return getRadius() * 2;
    }
}
