package com.bobin.somemapapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.ui.fragment.DepositionPointsListFragment;
import com.bobin.somemapapp.ui.fragment.MapFragment;

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
        List<Fragment> fragments = fm.getFragments();

        if (fragments == null)
            return;
        for (Fragment fragment : fragments) {
            if (fragment == null)
                continue;
            if (fragment instanceof DepositionPointsListFragment) {
                ((DepositionPointsListFragment) fragment).onPointsUpdated(points, userLocation);
                return;
            }
        }
    }
}
