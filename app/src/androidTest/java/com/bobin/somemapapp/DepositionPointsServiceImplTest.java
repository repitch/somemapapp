package com.bobin.somemapapp;

import com.bobin.somemapapp.helper.DataGenerator;
import com.bobin.somemapapp.infrastructure.DepositionPointsServiceImpl;
import com.bobin.somemapapp.model.response.DepositionPointResponse;
import com.bobin.somemapapp.model.response.DepositionPointsResponse;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.stub.PointsCacheStub;
import com.bobin.somemapapp.stub.TinkoffApiStub;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class DepositionPointsServiceImplTest {
    private DepositionPointsServiceImpl pointsService;
    private TinkoffApiStub tinkoffApi;
    private PointsCacheStub pointsCacheStub;

    @Before
    public void setUp() {
        tinkoffApi = new TinkoffApiStub();
        pointsCacheStub = new PointsCacheStub();
        pointsService = new DepositionPointsServiceImpl(tinkoffApi, pointsCacheStub);
    }

    @Test
    public void loadFromCacheById() {
        List<DepositionPoint> depositionPoints = DataGenerator.generatePoints(1);
        pointsCacheStub.savePoints(0, 0, 1, depositionPoints);

        TestObserver<List<DepositionPoint>> subscriber = new TestObserver<>();
        pointsService.getPoints(0, 0, 1).subscribe(subscriber);

        subscriber.assertValue(result -> result.size() == 1
                && result.get(0).getExternalId().equals(depositionPoints.get(0).getExternalId()));
        assertFalse(tinkoffApi.isCalledPoints());
    }

    @Test
    public void cacheEmptyLoadFromApi() {
        DepositionPointsResponse expectedResponse = DataGenerator.generatePointsResponse();
        DepositionPointResponse pointResponse = expectedResponse.getPayload().get(0);
        tinkoffApi.setNextResponse(expectedResponse);
        cacheExpired(); // потому что кэш пустой

        TestObserver<List<DepositionPoint>> subscriber = new TestObserver<>();
        pointsService.getPoints(0, 0, 1).subscribe(subscriber);

        subscriber.assertValue(result -> result.size() == 1
                && result.get(0).getExternalId().equals(pointResponse.getExternalId()));
        assertTrue(tinkoffApi.isCalledPoints());
    }

    @Test
    public void cacheExpiredLoadFromApi() {
        List<DepositionPoint> depositionPoints = DataGenerator.generatePoints(1);
        pointsCacheStub.savePoints(0, 0, 1, depositionPoints);
        cacheExpired();

        DepositionPointsResponse expectedResponse = DataGenerator.generatePointsResponse();
        DepositionPointResponse pointResponse = expectedResponse.getPayload().get(0);
        pointResponse.setExternalId(depositionPoints.get(0).getExternalId());
        tinkoffApi.setNextResponse(expectedResponse);

        TestObserver<List<DepositionPoint>> subscriber = new TestObserver<>();
        pointsService.getPoints(0, 0, 1).subscribe(subscriber);

        subscriber.assertValue(result -> result.size() == 1
                && result.get(0).getExternalId().equals(pointResponse.getExternalId()));
        assertTrue(tinkoffApi.isCalledPoints());
    }

    private void cacheExpired() {
        pointsCacheStub.setExpired(true);
    }
}
