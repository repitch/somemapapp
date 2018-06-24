package com.bobin.somemapapp.model;

public class MapCoordinates {
    private double latitude;
    private double longitude;

    public MapCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
