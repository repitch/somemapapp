package com.bobin.somemapapp.storage;

import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.model.tables.PointsCircle;
import com.bobin.somemapapp.model.tables.PointsToCircle;
import com.bobin.somemapapp.utils.GoogleMapUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmResults;

public class PointsCacheImpl implements PointsCache {
    private DbRepository database;

    public PointsCacheImpl() {
        database = new DbRepositoryImpl();
    }

    @Override
    public void savePoints(double latitude, double longitude, int radius,
                           List<DepositionPoint> points) {
        if (points.size() == 0)
            return;

        PointsCircle pointsCircle = new PointsCircle(latitude, longitude, radius);
        List<PointsCircle> circlesToDelete = findNestedCirclesOrNull(pointsCircle);

        terminateCirclesAndNestedPoints(circlesToDelete);

        List<PointsToCircle> links = createPointsToCircleLinks(pointsCircle, points);

        database.saveOrUpdate(pointsCircle);
        database.saveOrUpdate(points);
        database.saveOrUpdate(links);

        // просто всегда обновляю точки и все - владеющий круг все равно будет старше, и если он устареет, то удалим весь круг вместе с новыми точками
        // если внутри моего круга есть круг ЦЕЛИКОМ, то нафиг удаляем его с содержимым
    }

    @Nullable
    @Override
    public List<DepositionPoint> getPointsOrNull(double latitude, double longitude, int radius) {
        PointsCircle targetCircle = new PointsCircle(latitude, longitude, radius);
        PointsCircle outerCircle = findOuterCircleOrNull(targetCircle);
        if (outerCircle == null)
            return null;
        if (timeExpired(outerCircle)) {
            terminateCircleAndNestedPoints(outerCircle);
            return null;
        }
        return loadPointsFromCircle(outerCircle, targetCircle);
        // если нашли круг, в который полностью содержит наш круг и он не старый, то возвращаем его узлы
        // если он старый, то удаляем его и возвращаем null
    }

    @SuppressWarnings("ConstantConditions")
    private List<DepositionPoint> loadPointsFromCircle(PointsCircle outerCircle,
                                                       PointsCircle targetCircle) {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<PointsToCircle> links = realm.where(PointsToCircle.class)
                .equalTo("circleId", outerCircle.getId())
                .findAll();

        String[] ids = new String[links.size()];
        for (int i = 0; i < links.size(); ++i)
            ids[i] = links.get(i).getDepositionPointExternalId();

        RealmResults<DepositionPoint> allPoints = realm.where(DepositionPoint.class)
                .in("externalId", ids)
                .findAll();

        List<DepositionPoint> subSet = new ArrayList<>();
        for (DepositionPoint point : allPoints)
            if (pointInsideCircle(targetCircle, point))
                subSet.add(point);
        return subSet;
    }

    private List<PointsToCircle> createPointsToCircleLinks(PointsCircle pointsCircle, List<DepositionPoint> points) {
        List<PointsToCircle> result = new ArrayList<>(points.size());
        for (DepositionPoint point : points)
            result.add(new PointsToCircle(point.getExternalId(), pointsCircle.getId()));

        return result;
    }

    private void terminateCirclesAndNestedPoints(List<PointsCircle> circlesToDelete) {
        for (PointsCircle circle : circlesToDelete)
            terminateCircleAndNestedPoints(circle);
    }

    @SuppressWarnings("ConstantConditions")
    private void terminateCircleAndNestedPoints(PointsCircle circleToDelete) {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<PointsToCircle> links = realm.where(PointsToCircle.class)
                .equalTo("circleId", circleToDelete.getId())
                .findAll();

        String[] ids = new String[links.size()];
        for (int i = 0; i < links.size(); ++i)
            ids[i] = links.get(i).getDepositionPointExternalId();

        RealmResults<DepositionPoint> points = realm.where(DepositionPoint.class)
                .in("externalId", ids)
                .findAll();

        realm.executeTransaction(r -> {
            circleToDelete.deleteFromRealm();
            for (PointsToCircle link : links)
                link.deleteFromRealm();
            for (DepositionPoint point : points)
                point.deleteFromRealm();
        });
    }

    @Nullable
    private PointsCircle findOuterCircleOrNull(PointsCircle targetCircle) {
        List<PointsCircle> allCircles = database.getAll(PointsCircle.class);
        for (PointsCircle circle : allCircles) {
            if (circle.contains(targetCircle))
                return circle;
        }
        return null;
    }
 
    @Nonnull
    private List<PointsCircle> findNestedCirclesOrNull(PointsCircle targetCircle) {
        List<PointsCircle> result = new ArrayList<>();
        List<PointsCircle> allCircles = database.getAll(PointsCircle.class);
        for (PointsCircle circle : allCircles) {
            if (targetCircle.contains(circle))
                result.add(circle);
        }
        return result;
    }

    private boolean timeExpired(PointsCircle circle) {
        long now = System.currentTimeMillis();
        return now - circle.getTimestamp() > 1000 * 60 * 10;
    }

    private boolean pointInsideCircle(PointsCircle circle, DepositionPoint point) {
        return GoogleMapUtils.distanceBetween(circle.getCenterMapCoordinates(), point.getMapCoordinates()) < circle.getRadius();
    }
}
