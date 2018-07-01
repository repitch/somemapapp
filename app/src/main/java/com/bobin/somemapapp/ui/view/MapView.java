package com.bobin.somemapapp.ui.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPoint;

import java.util.List;

@StateStrategyType(SingleStateStrategy.class)
public interface MapView extends MvpView {
    void setMyLocationButtonEnabled(Boolean value);

    void showPins(List<DepositionPoint> pins);

    void showBottomSheet(DepositionPoint point, String name, String iconUrl);

    void moveToPoint(MapCoordinates coordinates);

    void showSnackbar(String message);

    void inProgress(boolean value);
}

