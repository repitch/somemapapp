package com.bobin.somemapapp;

import android.support.test.runner.AndroidJUnit4;

import com.bobin.somemapapp.helper.Config;
import com.bobin.somemapapp.helper.DataGenerator;
import com.bobin.somemapapp.helper.MyAsserts;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.model.tables.PointsCircle;
import com.bobin.somemapapp.model.tables.PointsToCircle;
import com.bobin.somemapapp.storage.PointsCacheImpl;
import com.bobin.somemapapp.stub.TestClock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.realm.Realm;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class PointsCacheImplTest {
    private PointsCacheImpl pointsCache;
    private TestClock clock;
    private static final long TEN_MINUTES = 1000 * 60 * 10;

    @Before
    public void setUp() {
        Config.initRealm();

        clock = new TestClock(42);
        pointsCache = new PointsCacheImpl(clock);
    }

    @Test
    public void simpleCircle() {
        int radius = 80000; // meters
        List<DepositionPoint> depositionPoints = DataGenerator.asList(
                DataGenerator.depositionPointAt(0.5, 0.5), //distance < 80000
                DataGenerator.depositionPointAt(-0.5, 0.5) //distance < 80000
        );
        pointsCache.savePoints(0, 0, radius, depositionPoints);

        List<DepositionPoint> points = pointsCache.getPointsOrNull(0, 0, radius);

        assertNotNull(points);
        assertEquals(2, points.size());
        MyAsserts.assertAny(points, x -> x.getExternalId().equals(depositionPoints.get(0).getExternalId()));
        MyAsserts.assertAny(points, x -> x.getExternalId().equals(depositionPoints.get(1).getExternalId()));
    }

    @Test
    public void smallerCircle() {
        int radius = 20000; // meters
        List<DepositionPoint> depositionPoints = DataGenerator.asList(
                DataGenerator.depositionPointAt(0.1, 0.1), //distance ~15690
                DataGenerator.depositionPointAt(0.2, -0.2) //distance ~31380
        );
        pointsCache.savePoints(0, 0, radius, depositionPoints);

        List<DepositionPoint> points = pointsCache.getPointsOrNull(0, 0, radius);

        assertNotNull(points);
        assertEquals(1, points.size());
        assertEquals(depositionPoints.get(0).getExternalId(), points.get(0).getExternalId());
    }

    @Test
    public void circleExpired() {
        int radius = 20000; // meters
        List<DepositionPoint> depositionPoints = DataGenerator.asList(
                DataGenerator.depositionPointAt(0.1, 0.1) //distance ~15690
        );
        pointsCache.savePoints(0, 0, radius, depositionPoints);
        clock.addMillis(TEN_MINUTES + 500);

        List<DepositionPoint> points = pointsCache.getPointsOrNull(0, 0, radius);

        assertNull(points);
        Realm realm = Realm.getDefaultInstance();
        long pointsCount = realm.where(DepositionPoint.class).count();
        long circlesCount = realm.where(PointsCircle.class).count();
        long linksCount = realm.where(PointsToCircle.class).count();
        realm.close();
        assertEquals(0, pointsCount);
        assertEquals(0, circlesCount);
        assertEquals(0, linksCount);
    }

    @Test
    public void circlesIntersect() {
        DepositionPoint point1 = DataGenerator.depositionPointAt(0, -0.75);
        DepositionPoint point2 = DataGenerator.depositionPointAt(0, 0);
        DepositionPoint point3 = DataGenerator.depositionPointAt(0, 0.75);
        PointsCircle circle1 = DataGenerator.circleAt(0, -0.5, 111000, clock);
        PointsCircle circle2 = DataGenerator.circleAt(0, 0.5, 111000, clock);
        save(circle1, point1, point2);
        save(circle2, point2, point3);

        List<DepositionPoint> pointsFromCircle1 = load(circle1);
        List<DepositionPoint> pointsFromCircle2 = load(circle2);

        assertNotNull(pointsFromCircle1);
        assertNotNull(pointsFromCircle2);
        assertEquals(2, pointsFromCircle1.size());
        assertEquals(2, pointsFromCircle2.size());
        MyAsserts.assertAny(pointsFromCircle1, x -> x.getExternalId().equals(point1.getExternalId()));
        MyAsserts.assertAny(pointsFromCircle1, x -> x.getExternalId().equals(point2.getExternalId()));
        MyAsserts.assertAny(pointsFromCircle2, x -> x.getExternalId().equals(point2.getExternalId()));
        MyAsserts.assertAny(pointsFromCircle2, x -> x.getExternalId().equals(point3.getExternalId()));
    }

    @Test
    public void circlesIntersectAndOneExpired() {
        DepositionPoint point1 = DataGenerator.depositionPointAt(0, -0.75);
        DepositionPoint point2 = DataGenerator.depositionPointAt(0, 0);
        DepositionPoint point3 = DataGenerator.depositionPointAt(0, 0.75);
        PointsCircle circle1 = DataGenerator.circleAt(0, -0.5, 111000, clock);
        PointsCircle circle2 = DataGenerator.circleAt(0, 0.5, 111000, clock);

        save(circle1, point1, point2);
        clock.addMillis(TEN_MINUTES / 2);
        save(circle2, point2, point3);
        clock.setCurrent(TEN_MINUTES + 500);

        List<DepositionPoint> pointsFromCircle1 = load(circle1);
        List<DepositionPoint> pointsFromCircle2 = load(circle2);

        assertNull(pointsFromCircle1);
        assertNotNull(pointsFromCircle2);
        assertEquals(2, pointsFromCircle2.size());
        MyAsserts.assertAny(pointsFromCircle2, x -> x.getExternalId().equals(point2.getExternalId()));
        MyAsserts.assertAny(pointsFromCircle2, x -> x.getExternalId().equals(point3.getExternalId()));
    }

    private List<DepositionPoint> load(PointsCircle circle) {
        return pointsCache.getPointsOrNull(
                circle.getCenterLatitude(),
                circle.getCenterLongitude(),
                circle.getRadius());
    }

    private void save(PointsCircle circle, DepositionPoint... points) {
        pointsCache.savePoints(circle.getCenterLatitude(),
                circle.getCenterLongitude(),
                circle.getRadius(),
                DataGenerator.asList(points));
    }
}
