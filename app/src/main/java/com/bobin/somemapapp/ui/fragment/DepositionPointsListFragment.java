package com.bobin.somemapapp.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bobin.somemapapp.R;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.presenter.DepositionPointsListPresenter;
import com.bobin.somemapapp.ui.activity.DepositionPointDetailActivity;
import com.bobin.somemapapp.ui.adapter.DepositionPointsListAdapter;
import com.bobin.somemapapp.ui.view.DepositionPointsListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DepositionPointsListFragment
        extends MvpAppCompatFragment
        implements DepositionPointsListView, DepositionPointsListAdapter.PointClickListener {
    @BindView(R.id.deposition_points_list)
    RecyclerView recyclerView;

    @InjectPresenter
    DepositionPointsListPresenter presenter;
    private Unbinder unbinder;
    private DepositionPointsListAdapter adapter;
    private MapCoordinates userLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new DepositionPointsListAdapter();
        adapter.setClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deposition_points_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setExitTransition(new Fade());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void onPointsUpdated(List<DepositionPoint> points, MapCoordinates userLocation) {
        presenter.updateList(points, userLocation);
        this.userLocation = userLocation;
    }

    @Override
    public void updateList(List<DepositionPointsListAdapter.BindData> data) {
        adapter.setDataset(data, userLocation);
    }

    @Override
    public void updateElement(DepositionPointsListAdapter.BindData data, int position) {
        adapter.updateElement(data, position);
    }

    @Override
    public void onClickPoint(DepositionPoint point, View iconView, int position) {
        DepositionPointDetailActivity.start(getActivity(), point, userLocation, iconView);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }
}
