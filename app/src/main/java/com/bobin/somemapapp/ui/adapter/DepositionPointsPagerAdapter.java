package com.bobin.somemapapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.ui.fragment.DepositionPointsListFragment;
import com.bobin.somemapapp.ui.fragment.MapFragment;
import com.bobin.somemapapp.utils.ViewUtils;

import java.util.List;

public class DepositionPointsPagerAdapter extends FragmentPagerAdapter {
    private final FragmentManager fm;

    public DepositionPointsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MapFragment();
            case 1:
                return new DepositionPointsListFragment();
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int getCount() {
        return 2;
    }

    public void updatePointsList(List<DepositionPoint> points, MapCoordinates userLocation) {
        DepositionPointsListFragment fragment = ViewUtils.findFragment(fm, DepositionPointsListFragment.class);
        if (fragment != null)
            fragment.onPointsUpdated(points, userLocation);
    }
}
