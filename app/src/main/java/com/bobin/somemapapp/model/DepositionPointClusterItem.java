package com.bobin.somemapapp.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class DepositionPointClusterItem implements ClusterItem {
    private final String title;
    private final LatLng position;

    public DepositionPointClusterItem(double latitude, double longitude, String title) {
        position = new LatLng(latitude, longitude);
        this.title = title;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
