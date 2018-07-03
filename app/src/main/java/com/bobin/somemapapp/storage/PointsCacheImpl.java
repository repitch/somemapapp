package com.bobin.somemapapp.storage;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bobin.somemapapp.infrastructure.Clock;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.model.tables.PointsCircle;
import com.bobin.somemapapp.model.tables.PointsToCircle;
import com.bobin.somemapapp.utils.GoogleMapUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.bobin.somemapapp.utils.CollectionUtils.all;
import static com.bobin.somemapapp.utils.CollectionUtils.filter;

public class PointsCacheImpl implements PointsCache {
    private Clock clock;

    public PointsCacheImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void savePoints(double latitude, double longitude, int radius,
                           List<DepositionPoint> points) {
        if (points.size() == 0)
            return;

        PointsCircle pointsCircle = new PointsCircle(latitude, longitude, radius, clock);
        List<PointsCircle> circlesToDelete = findNestedCircles(pointsCircle);

        terminateCirclesAndNestedPoints(circlesToDelete, true);

        List<PointsToCircle> links = createPointsToCircleLinks(pointsCircle, points);

        Realm.getDefaultInstance().executeTransaction(r -> {
            r.insertOrUpdate(pointsCircle);
            r.insertOrUpdate(points);
            r.insertOrUpdate(links);
        });
    }

    @Nullable
    @Override
    public List<DepositionPoint> getPointsOrNull(double latitude, double longitude, int radius) {
        Log.d("PointsCacheImpl", "getPointsOrNull lat: " + latitude + " lon: " + longitude + " rad: " + radius);
        PointsCircle targetCircle = new PointsCircle(latitude, longitude, radius, clock);
        PointsCircle outerCircle = findOuterCircleOrNull(targetCircle);
        if (outerCircle == null) {
            Log.d("PointsCacheImpl", "no cache");
            return null; // Collections.emptyList
        }
        if (timeExpired(outerCircle)) {
            Log.d("PointsCacheImpl", "circle expired");
            terminateCircleAndNestedPoints(outerCircle, false);
            return null;
        }
        Log.d("PointsCacheImpl", "loadPointsFromCircle");
        return loadPointsFromCircle(outerCircle, targetCircle);
    }

    @NonNull
    @SuppressWarnings("ConstantConditions")
    private List<DepositionPoint> loadPointsFromCircle(PointsCircle outerCircle,
                                                       PointsCircle targetCircle) {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<PointsToCircle> links = realm.where(PointsToCircle.class)
                .equalTo("circleId", outerCircle.getId())
                .findAll();

        String[] ids = getPointsExternalIds(links);

        RealmResults<DepositionPoint> allPoints = realm.where(DepositionPoint.class)
                .in("externalId", ids)
                .findAll();

        return GoogleMapUtils.pointsFromCircle(targetCircle, realm.copyFromRealm(allPoints));
    }

    @NonNull
    private List<PointsToCircle> createPointsToCircleLinks(PointsCircle pointsCircle, List<DepositionPoint> points) {
        List<PointsToCircle> result = new ArrayList<>(points.size());
        for (DepositionPoint point : points)
            result.add(new PointsToCircle(point.getExternalId(), pointsCircle.getId()));

        return result;
    }

    private void terminateCirclesAndNestedPoints(List<PointsCircle> circlesToDelete, boolean deleteAllPoints) {
        for (PointsCircle circle : circlesToDelete)
            terminateCircleAndNestedPoints(circle, deleteAllPoints);
    }

    @SuppressWarnings("ConstantConditions")
    private void terminateCircleAndNestedPoints(PointsCircle circleToDelete, boolean deleteAllPoints) {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<PointsToCircle> links = realm.where(PointsToCircle.class)
                .equalTo("circleId", circleToDelete.getId())
                .findAll();

        RealmResults<DepositionPoint> points = realm.where(DepositionPoint.class)
                .in("externalId", getPointsExternalIds(links))
                .findAll();

        List<DepositionPoint> pointsToDelete = deleteAllPoints
                ? points
                : filterPointsToDelete(circleToDelete, points);

        realm.executeTransaction(r -> {
            circleToDelete.deleteFromRealm();
            for (PointsToCircle link : links)
                link.deleteFromRealm();
            for (DepositionPoint point : pointsToDelete)
                point.deleteFromRealm();
        });
    }

    @NonNull
    private List<DepositionPoint> filterPointsToDelete(PointsCircle circleToDelete,
                                                       List<DepositionPoint> points) {
        Realm realm = Realm.getDefaultInstance();

        long now = clock.currentTimeInMillis();
        final long tenMinutes = 1000 * 60 * 10;

        RealmResults<PointsCircle> allCircles = realm.where(PointsCircle.class)
                .notEqualTo("id", circleToDelete.getId())
                .greaterThan("timestamp", now - tenMinutes)
                .findAll();

        final List<PointsCircle> intersectedCircles = filter(allCircles, x -> x.intersect(circleToDelete));
        return filter(points, p -> all(intersectedCircles, c -> !c.contains(p)));
    }

    @NonNull
    private String[] getPointsExternalIds(List<PointsToCircle> links) {
        String[] ids = new String[links.size()];
        for (int i = 0; i < links.size(); ++i)
            ids[i] = links.get(i).getDepositionPointExternalId();
        return ids;
    }

    @Nullable
    private PointsCircle findOuterCircleOrNull(PointsCircle targetCircle) {
        Realm realm = Realm.getDefaultInstance();
        List<PointsCircle> allCircles = realm.where(PointsCircle.class).findAll();

        for (PointsCircle circle : allCircles) {
            if (circle.contains(targetCircle) || circle.isTheSame(targetCircle))
                return circle;
        }
        return null;
    }

    @NonNull
    private List<PointsCircle> findNestedCircles(PointsCircle targetCircle) {
        List<PointsCircle> result = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();
        List<PointsCircle> allCircles = realm.where(PointsCircle.class).findAll();

        for (PointsCircle circle : allCircles) {
            if (targetCircle.contains(circle))
                result.add(circle);
        }
        return result;
    }

    private boolean timeExpired(PointsCircle circle) {
        long now = clock.currentTimeInMillis();
        return now - circle.getTimestamp() > 1000 * 60 * 10;
    }
}
