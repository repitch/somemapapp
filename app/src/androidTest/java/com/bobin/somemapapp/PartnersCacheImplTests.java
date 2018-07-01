package com.bobin.somemapapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.bobin.somemapapp.helper.DataGenerator;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.storage.KeyValueStorage;
import com.bobin.somemapapp.storage.PartnersCacheImpl;
import com.bobin.somemapapp.stub.InMemoryKeyValueStorage;
import com.bobin.somemapapp.stub.TestClock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PartnersCacheImplTests {
    private PartnersCacheImpl partnersCache;
    private KeyValueStorage keyValueStorage;
    private TestClock clock;

    @Before
    public void setUp() {
        Realm.init(InstrumentationRegistry.getTargetContext());
        RealmConfiguration testConfig = new RealmConfiguration.Builder()
                .inMemory()
                .name("test-realm")
                .build();

        Realm.setDefaultConfiguration(testConfig);
        clock = new TestClock(0);
        keyValueStorage = new InMemoryKeyValueStorage();
        partnersCache = new PartnersCacheImpl(keyValueStorage, clock);
    }

    @Test
    public void simpleSaveAndGet() {
        List<DepositionPartner> partners = DataGenerator.generatePartners(1);

        partnersCache.savePartners(partners);
        DepositionPartner partner = partnersCache.getPartnerByIdOrNull(partners.get(0).getId());

        assertNotNull(partner);
        assertEquals(partners.get(0).getId(), partner.getId());
    }

    @Test
    public void saveMultipleGetMultiple() {
        List<DepositionPartner> partners = DataGenerator.generatePartners(2);
        String[] ids = DataGenerator.ids(partners);

        partnersCache.savePartners(partners);
        HashMap<String, DepositionPartner> partnersMap = partnersCache.getPartnersByIdsOrNull(ids);

        assertNotNull(partnersMap);
        assertEquals(2, partnersMap.size());
        assertEquals(ids[0], partnersMap.get(ids[0]).getId());
        assertEquals(ids[1], partnersMap.get(ids[1]).getId());
    }

    @Test
    public void saveMultipleGetSingle() {
        List<DepositionPartner> partners = DataGenerator.generatePartners(2);
        String[] ids = DataGenerator.ids(partners);

        partnersCache.savePartners(partners);
        DepositionPartner partner = partnersCache.getPartnerByIdOrNull(ids[0]);

        assertNotNull(partner);
        assertEquals(ids[0], partner.getId());
    }

    @Test
    public void expired() {
        List<DepositionPartner> partners = DataGenerator.generatePartners(1);
        partnersCache.savePartners(partners);

        clock.addMillis(1000 * 60 * 10 + 500);
        boolean expired = partnersCache.isExpired();

        assertTrue(expired);
    }

    @Test
    public void despiteExpiredReturnPartner() {
        List<DepositionPartner> partners = DataGenerator.generatePartners(1);
        partnersCache.savePartners(partners);
        clock.addMillis(1000 * 60 * 10 + 500);

        DepositionPartner partner = partnersCache.getPartnerByIdOrNull(partners.get(0).getId());

        assertNotNull(partner);
    }
}
