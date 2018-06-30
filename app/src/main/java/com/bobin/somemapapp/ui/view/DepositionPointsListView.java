package com.bobin.somemapapp.ui.view;

import com.arellomobile.mvp.MvpView;
import com.bobin.somemapapp.ui.adapter.DepositionPointsListAdapter;

import java.util.List;

public interface DepositionPointsListView extends MvpView {
    void updateList(List<DepositionPointsListAdapter.BindData> data);

    void updateElement(DepositionPointsListAdapter.BindData data, int position);
}
