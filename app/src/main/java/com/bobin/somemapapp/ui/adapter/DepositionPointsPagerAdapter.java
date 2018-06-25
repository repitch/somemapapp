package com.bobin.somemapapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bobin.somemapapp.ui.fragment.DepositionPointsListFragment;
import com.bobin.somemapapp.ui.fragment.MapFragment;

public class DepositionPointsPagerAdapter extends FragmentPagerAdapter {
    public DepositionPointsPagerAdapter(FragmentManager fm) {
        super(fm);
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
}
