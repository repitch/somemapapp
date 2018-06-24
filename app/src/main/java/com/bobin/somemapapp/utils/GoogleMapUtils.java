package com.bobin.somemapapp.utils;

import android.location.Location;

import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.response.LocationResponse;
import com.bobin.somemapapp.model.tables.PointsCircle;
import com.google.android.gms.maps.model.LatLng;

public final class GoogleMapUtils {
    public static LatLng toLatLng(MapCoordinates coordinates) {
        return new LatLng(coordinates.getLatitude(), coordinates.getLongitude());
    }

    public static MapCoordinates toCoordinates(LatLng latLng) {
        return new MapCoordinates(latLng.latitude, latLng.longitude);
    }

    public static MapCoordinates toCoordinates(LocationResponse locationResponse) {
        return new MapCoordinates(locationResponse.getLatitude(), locationResponse.getLongitude());
    }

    public static float distanceBetween(PointsCircle circleA, PointsCircle circleB) {
        return distanceBetween(
                circleA.getCenterLatitude(),
                circleA.getCenterLongitude(),
                circleB.getCenterLatitude(),
                circleB.getCenterLongitude());
    }

    public static float distanceBetween(MapCoordinates pointA, MapCoordinates pointB) {
        return distanceBetween(
                pointA.getLatitude(),
                pointA.getLongitude(),
                pointB.getLatitude(),
                pointB.getLongitude());
    }

    private static float distanceBetween(double startLatitude, double startLongitude,
                                         double endLatitude, double endLongitude) {
        float[] results = new float[2];
        Location.distanceBetween(
                startLatitude,
                startLongitude,
                endLatitude,
                endLongitude,
                results);
        return results[0];
    }
}
