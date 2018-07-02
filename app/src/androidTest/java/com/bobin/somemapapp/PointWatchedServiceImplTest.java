package com.bobin.somemapapp;

import android.support.test.runner.AndroidJUnit4;

import com.bobin.somemapapp.helper.Config;
import com.bobin.somemapapp.helper.DataGenerator;
import com.bobin.somemapapp.infrastructure.PointWatchedServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PointWatchedServiceImplTest {
    private PointWatchedServiceImpl watchedService;

    @Before
    public void setUp() {
        Config.initRealm();

        watchedService = new PointWatchedServiceImpl();
    }

    @Test
    public void setWatchedWhenSaved() {
        String id = DataGenerator.randomString();
        watchedService.setWatched(id);

        boolean watched = watchedService.isWatched(id);

        assertTrue(watched);
    }

    @Test
    public void setWatchedWhenNotSaved() {
        String id = DataGenerator.randomString();

        boolean watched = watchedService.isWatched(id);

        assertFalse(watched);
    }
}
