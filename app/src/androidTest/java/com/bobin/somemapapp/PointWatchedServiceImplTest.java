package com.bobin.somemapapp;

import android.support.test.runner.AndroidJUnit4;

import com.bobin.somemapapp.helper.Config;
import com.bobin.somemapapp.helper.DataGenerator;
import com.bobin.somemapapp.infrastructure.PointWatchedServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
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

    @Test
    public void isWatchedForMultipleIdsWhenNotSaved() {
        String id1 = DataGenerator.randomString();
        String id2 = DataGenerator.randomString();

        HashSet<String> watched = watchedService.isWatched(DataGenerator.asList(id1, id2));

        assertNotNull(watched);
        assertEquals(0, watched.size());
    }

    @Test
    public void isWatchedForMultipleIds() {
        String id1 = DataGenerator.randomString();
        String id2 = DataGenerator.randomString();
        String id3 = DataGenerator.randomString();
        watchedService.setWatched(id1);
        watchedService.setWatched(id2);

        HashSet<String> watched = watchedService.isWatched(DataGenerator.asList(id1, id2, id3));

        assertNotNull(watched);
        assertEquals(2, watched.size());

        assertTrue(watched.contains(id1));
        assertTrue(watched.contains(id2));
        assertFalse(watched.contains(id3));
    }
}
