package com.bobin.somemapapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bobin.somemapapp.R;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.presenter.DepositionPointsListPresenter;
import com.bobin.somemapapp.ui.activity.DepositionPointDetailActivity;
import com.bobin.somemapapp.ui.adapter.DepositionPointsListAdapter;
import com.bobin.somemapapp.ui.view.DepositionPointsListView;

import java.util.HashMap;
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void updateList(List<DepositionPoint> points) {
        presenter.updateList(points);
    }

    @Override
    public void updateList(List<DepositionPoint> points, HashMap<String, String> icons) {
        adapter.setDataset(points, icons);
    }

    @Override
    public void onClickPoint(DepositionPoint point, View iconView) {
        DepositionPointDetailActivity.start(getActivity(), point, null, iconView);
    }
}
