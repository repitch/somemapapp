package com.bobin.somemapapp.ui.activity;

import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPoint;

import java.util.List;

public interface DepositionPointsChangedListener {
    void onChangeDepositionPoints(List<DepositionPoint> points, MapCoordinates userLocation);
}
