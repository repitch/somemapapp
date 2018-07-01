package com.bobin.somemapapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bobin.somemapapp.R;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.ui.adapter.DepositionPointsPagerAdapter;
import com.bobin.somemapapp.ui.fragment.DepositionPointsListFragment;
import com.bobin.somemapapp.ui.fragment.MapFragment;
import com.bobin.somemapapp.utils.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DepositionPointsActivity
        extends AppCompatActivity
        implements TabLayout.OnTabSelectedListener,
        ViewPager.OnPageChangeListener,
        DepositionPointsChangedListener {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private Unbinder unbinder;
    private DepositionPointsPagerAdapter fragmentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposition_points);
        unbinder = ButterKnife.bind(this);

        fragmentsAdapter = new DepositionPointsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentsAdapter);

        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.item_tab).setText(R.string.map), 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.item_tab).setText(R.string.points), 1);
        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(this);
        ViewUtils.changeAllTextViewsToCustomFont(toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        tabLayout.setScrollPosition(position, positionOffset, false);
    }

    @Override
    public void onPageSelected(int position) {
        tabLayout.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onChangeDepositionPoints(List<DepositionPoint> points, MapCoordinates userLocation) {
        fragmentsAdapter.updatePointsList(points, userLocation);
        tabLayout.getTabAt(1).setText(getString(R.string.points) + " (" + points.size() + ")");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DepositionPointsListFragment.DETAIL_REQUEST && resultCode == Activity.RESULT_OK) {
            MapCoordinates mapCoordinates = DepositionPointDetailActivity.tryExtractActivityResult(data);
            if (mapCoordinates == null)
                return;
            tabLayout.getTabAt(0).select();
            viewPager.setCurrentItem(0);
            MapFragment fragment = ViewUtils.findFragment(getSupportFragmentManager(), MapFragment.class);
            if (fragment != null)
                fragment.moveToPoint(mapCoordinates);
        }
    }
}
