package com.bobin.somemapapp.ui.view;

import com.arellomobile.mvp.MvpView;
import com.bobin.somemapapp.model.tables.DepositionPoint;

import java.util.List;

public interface MapView extends MvpView {
    void setMyLocationButtonEnabled(Boolean value);

    void showPins(List<DepositionPoint> pins);

    void showBottomSheet(DepositionPoint point, String name, String iconUrl);
}

