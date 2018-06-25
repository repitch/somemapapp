package com.bobin.somemapapp.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class DepositionPointClusterItem implements ClusterItem {
    private LatLng position;

    public DepositionPointClusterItem(MapCoordinates coordinates) {
        position = new LatLng(coordinates.getLatitude(), coordinates.getLongitude());
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
