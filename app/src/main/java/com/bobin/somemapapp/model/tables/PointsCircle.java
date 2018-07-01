package com.bobin.somemapapp.model.tables;

import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.utils.GoogleMapUtils;

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

    public MapCoordinates getCenterMapCoordinates() {
        return new MapCoordinates(centerLatitude, centerLongitude);
    }

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

    public boolean contains(PointsCircle circle) {
        float distance = GoogleMapUtils.distanceBetween(circle, this);
        return circle.getRadius() + distance < getRadius();
    }

    public boolean isTheSame(PointsCircle circle) {
        return this.getCenterLatitude() == circle.getCenterLatitude() &&
                this.getCenterLongitude() == circle.getCenterLongitude() &&
                this.getRadius() == circle.getRadius();
    }

    public boolean intersect(PointsCircle circle) {
        float distance = GoogleMapUtils.distanceBetween(circle, this);
        return distance < getRadius() + circle.getRadius();
    }

    public boolean contains(DepositionPoint point) {
        return GoogleMapUtils.pointInsideCircle(this, point);
    }
}
