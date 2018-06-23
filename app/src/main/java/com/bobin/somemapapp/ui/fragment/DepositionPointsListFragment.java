package com.bobin.somemapapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bobin.somemapapp.R;
import com.bobin.somemapapp.presenter.DepositionPointsListPresenter;
import com.bobin.somemapapp.ui.view.DepositionPointsListView;

public class DepositionPointsListFragment
        extends MvpAppCompatFragment
        implements DepositionPointsListView {

    @InjectPresenter
    DepositionPointsListPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deposition_points_list, container, false);
    }
}
