package com.bobin.somemapapp;

import com.bobin.somemapapp.helper.DataGenerator;
import com.bobin.somemapapp.infrastructure.PartnersServiceImpl;
import com.bobin.somemapapp.model.response.DepositionPartnerResponse;
import com.bobin.somemapapp.model.response.DepositionPartnersResponse;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.stub.PartnersCacheStub;
import com.bobin.somemapapp.stub.TinkoffApiStub;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class PartnersServiceImplTest {
    private PartnersServiceImpl partnersService;
    private TinkoffApiStub tinkoffApi;
    private PartnersCacheStub partnersCache;

    @Before
    public void setUp() {
        tinkoffApi = new TinkoffApiStub();
        partnersCache = new PartnersCacheStub();
        partnersService = new PartnersServiceImpl(tinkoffApi, partnersCache);
    }

    @Test
    public void loadFromCacheById() {
        List<DepositionPartner> partners = DataGenerator.generatePartners(1);
        partnersCache.savePartners(partners);

        TestObserver<DepositionPartner> subscriber = new TestObserver<>();
        partnersService.getPartnerById(partners.get(0).getId()).subscribe(subscriber);

        subscriber.assertValue(result -> result.getId().equals(partners.get(0).getId()));
    }

    @Test
    public void cacheEmptyLoadFromApi() {
        DepositionPartnersResponse expectedResponse = DataGenerator.generatePartnersResponse(1);
        DepositionPartnerResponse partnerResponse = expectedResponse.getPayload().get(0);
        tinkoffApi.setNextResponse(expectedResponse);
        cacheExpired(); // потому что кэш пустой

        TestObserver<DepositionPartner> subscriber = new TestObserver<>();
        partnersService.getPartnerById(partnerResponse.getId()).subscribe(subscriber);

        subscriber.assertValue(result -> result.getId().equals(partnerResponse.getId()));
        assertTrue(tinkoffApi.isCalledPartners());
    }

    @Test
    public void cacheExpiredLoadFromApi() {
        List<DepositionPartner> depositionPartners = DataGenerator.generatePartners(1);
        partnersCache.savePartners(depositionPartners);
        cacheExpired();

        DepositionPartnersResponse expectedResponse = DataGenerator.generatePartnersResponse(1);
        DepositionPartnerResponse partnerResponse = expectedResponse.getPayload().get(0);
        partnerResponse.setId(depositionPartners.get(0).getId());
        tinkoffApi.setNextResponse(expectedResponse);

        TestObserver<DepositionPartner> subscriber = new TestObserver<>();
        partnersService.getPartnerById(partnerResponse.getId()).subscribe(subscriber);

        subscriber.assertValue(result -> result.getId().equals(partnerResponse.getId()));
        assertTrue(tinkoffApi.isCalledPartners());
    }

    @Test
    public void multipleIdsAndCacheEmptyLoadFromApi() {
        DepositionPartnersResponse expectedResponse = DataGenerator.generatePartnersResponse(3);
        tinkoffApi.setNextResponse(expectedResponse);
        cacheExpired(); // потому что кэш пустой

        TestObserver<HashMap<String, DepositionPartner>> subscriber = new TestObserver<>();
        List<String> ids = DataGenerator.ids(expectedResponse, 2);
        partnersService.getPartnersByIds(ids).subscribe(subscriber);

        subscriber.assertValue(result -> {
            assertEquals(2, result.size());
            assertEquals(ids.get(0), result.get(ids.get(0)).getId());
            assertEquals(ids.get(1), result.get(ids.get(1)).getId());
            return true;
        });
        assertTrue(tinkoffApi.isCalledPartners());
    }

    @Test
    public void multipleIdsAndCacheExpiredLoadFromApi() {
        DepositionPartnersResponse expectedResponse = DataGenerator.generatePartnersResponse(3);
        List<DepositionPartner> convertedResponse = DataGenerator.convert(expectedResponse);

        partnersCache.savePartners(convertedResponse);
        cacheExpired(); // потому что кэш пустой
        tinkoffApi.setNextResponse(expectedResponse);

        TestObserver<HashMap<String, DepositionPartner>> subscriber = new TestObserver<>();
        List<String> ids = DataGenerator.ids(expectedResponse, 2);
        partnersService.getPartnersByIds(ids).subscribe(subscriber);

        subscriber.assertValue(result -> {
            assertEquals(2, result.size());
            assertEquals(ids.get(0), result.get(ids.get(0)).getId());
            assertEquals(ids.get(1), result.get(ids.get(1)).getId());
            return true;
        });
        assertTrue(tinkoffApi.isCalledPartners());
    }

    private void cacheExpired() {
        partnersCache.setExpired(true);
    }
}
