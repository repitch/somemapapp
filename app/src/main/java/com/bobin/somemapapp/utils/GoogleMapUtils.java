package com.bobin.somemapapp.utils;

import android.content.Context;
import android.location.Location;

import com.bobin.somemapapp.R;
import com.bobin.somemapapp.infrastructure.Clock;
import com.bobin.somemapapp.model.CameraBounds;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.model.tables.PointsCircle;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public final class GoogleMapUtils {
    public static LatLng toLatLng(MapCoordinates coordinates) {
        return new LatLng(coordinates.getLatitude(), coordinates.getLongitude());
    }

    public static MapCoordinates toCoordinates(Location location) {
        return new MapCoordinates(location.getLatitude(), location.getLongitude());
    }

    public static MapCoordinates toCoordinates(LatLng latLng) {
        return new MapCoordinates(latLng.latitude, latLng.longitude);
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

    public static List<DepositionPoint> pointsFromCircle(PointsCircle circle, List<DepositionPoint> points) {
        if (points == null)
            return new ArrayList<>(0);
        List<DepositionPoint> result = new ArrayList<>();
        for (DepositionPoint point : points)
            if (pointInsideCircle(circle, point))
                result.add(point);
        return result;
    }

    private static boolean pointInsideCircle(PointsCircle circle, MapCoordinates point) {
        float distance = distanceBetween(circle.getCenterMapCoordinates(), point);
        return distance < circle.getRadius();
    }

    public static boolean pointInsideCircle(PointsCircle circle, DepositionPoint point) {
        return pointInsideCircle(circle, point.getMapCoordinates());
    }

    public static PointsCircle toCircle(CameraBounds bounds, Clock clock) {
        float radius = GoogleMapUtils.distanceBetween(bounds.getLeftTop(), bounds.getCenter());

        return new PointsCircle(
                bounds.getCenter().getLatitude(),
                bounds.getCenter().getLongitude(),
                (int) radius,
                clock);
    }

    public static String distanceToString(Context context, int meters) {
        if (meters < 1000)
            return meters + " " + context.getString(R.string.meters); // получение форматированной строки + plurals

        String format = new DecimalFormat("#.#").format(meters / 1000d);
        return format + " " + context.getString(R.string.km);
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
