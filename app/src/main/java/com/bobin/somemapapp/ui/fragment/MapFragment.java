package com.bobin.somemapapp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bobin.somemapapp.R;
import com.bobin.somemapapp.model.CameraBounds;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.response.DepositionPointResponse;
import com.bobin.somemapapp.presenter.MapPresenter;
import com.bobin.somemapapp.ui.view.MapView;
import com.bobin.somemapapp.utils.GoogleMapUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.List;

public class MapFragment
        extends MvpAppCompatFragment
        implements OnMapReadyCallback,
        MapView,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveCanceledListener {

    @InjectPresenter
    MapPresenter presenter;

    private GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        FragmentManager fm = activity.getSupportFragmentManager();
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        fm.beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        presenter.mapIsReady(getActivity());

        map.setOnCameraIdleListener(this);
        map.setOnCameraMoveCanceledListener(this);
    }

    @Override
    public void showDialog(String title, String text) {
        Context context = getContext();
        if (context == null)
            return;
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(text)
                .show();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void setMyLocationButtonEnabled(Boolean value) {
        if (map != null) {
            map.setMyLocationEnabled(value);
            map.getUiSettings().setMyLocationButtonEnabled(value);
        }
    }

    @Override
    public void showPins(List<DepositionPointResponse> pins) {
        clearPins();
        for (DepositionPointResponse pin : pins) {
            addPin(new MapCoordinates(
                    pin.getLocation().getLatitude(),
                    pin.getLocation().getLongitude()));
        }
    }

    @Override
    public void addPin(MapCoordinates coordinates) {
        LatLng latLng = GoogleMapUtils.toLatLng(coordinates);
        map.addMarker(new MarkerOptions().position(latLng));
    }

    @Override
    public void clearPins() {
        map.clear();
    }

    @Override
    public void addRadius(MapCoordinates center, double radius) {
        Circle currentCircle = map.addCircle(
                new CircleOptions()
                        .center(GoogleMapUtils.toLatLng(center))
                        .radius(radius));
    }

    @Override
    public void onCameraIdle() {
        cameraStops();
    }

    @Override
    public void onCameraMoveCanceled() {
        cameraStops();
    }

    private void cameraStops() {
        if (map == null)
            return;

        VisibleRegion visibleRegion = map.getProjection().getVisibleRegion();
        CameraBounds cameraBounds = new CameraBounds(
                GoogleMapUtils.toCoordinates(visibleRegion.farLeft),
                GoogleMapUtils.toCoordinates(visibleRegion.nearRight),
                GoogleMapUtils.toCoordinates(visibleRegion.latLngBounds.getCenter()));

        presenter.mapCameraStops(cameraBounds);
    }
}
