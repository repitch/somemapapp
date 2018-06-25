package com.bobin.somemapapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.ui.fragment.DepositionPointsListFragment;
import com.bobin.somemapapp.ui.fragment.MapFragment;

import java.util.List;

public class DepositionPointsPagerAdapter extends FragmentPagerAdapter {
    public DepositionPointsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private DepositionPointsListFragment depositionPointsListFragment;

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MapFragment();
            case 1:
                depositionPointsListFragment = new DepositionPointsListFragment();
                return depositionPointsListFragment;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int getCount() {
        return 2;
    }

    public void updatePointsList(List<DepositionPoint> points) {
        if (depositionPointsListFragment != null)
            depositionPointsListFragment.updateList(points);
    }
}
