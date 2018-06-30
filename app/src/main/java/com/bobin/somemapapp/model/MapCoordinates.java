package com.bobin.somemapapp.model;

import java.io.Serializable;

public class MapCoordinates implements Serializable {
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
