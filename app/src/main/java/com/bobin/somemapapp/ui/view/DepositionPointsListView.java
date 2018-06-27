package com.bobin.somemapapp.ui.view;

import com.arellomobile.mvp.MvpView;
import com.bobin.somemapapp.model.tables.DepositionPoint;

import java.util.HashMap;
import java.util.List;

public interface DepositionPointsListView extends MvpView {
    void updateList(List<DepositionPoint> points, HashMap<String, String> icons);
}
