package com.bobin.somemapapp.ui.activity;

import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bobin.somemapapp.R;
import com.bobin.somemapapp.presenter.DepositionPointDetailPresenter;
import com.bobin.somemapapp.ui.view.DepositionPointDetailView;

public class DepositionPointDetailActivity
        extends MvpAppCompatActivity
        implements DepositionPointDetailView {

    @InjectPresenter
    DepositionPointDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposition_point_detail);
    }
}
