package com.bobin.somemapapp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bobin.somemapapp.R;
import com.bobin.somemapapp.model.CameraBounds;
import com.bobin.somemapapp.model.DepositionPointClusterItem;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.presenter.MapPresenter;
import com.bobin.somemapapp.ui.activity.DepositionPointDetailActivity;
import com.bobin.somemapapp.ui.activity.DepositionPointsChangedListener;
import com.bobin.somemapapp.ui.view.MapView;
import com.bobin.somemapapp.utils.GoogleMapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class MapFragment
        extends MvpAppCompatFragment
        implements OnMapReadyCallback,
        MapView,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationChangeListener {

    @InjectPresenter
    MapPresenter presenter;

    private GoogleMap map;
    private ClusterManager<DepositionPointClusterItem> clusterManager;
    private DepositionPointsChangedListener depositionPointsChangedListener;
    private MapCoordinates currentUserLocation;
    private boolean firstLaunch;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FragmentActivity activity = getActivity();
        if (activity instanceof DepositionPointsChangedListener) {
            depositionPointsChangedListener = (DepositionPointsChangedListener) activity;
        }
    }

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

        firstLaunch = savedInstanceState == null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        clusterManager = new ClusterManager<>(getContext(), googleMap);
        clusterManager.setAlgorithm(new GridBasedAlgorithm<>());
        clusterManager.setAnimation(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        presenter.mapIsReady(getActivity());
        map.setOnMyLocationChangeListener(this);
        map.setOnCameraIdleListener(this);
        map.setOnCameraMoveCanceledListener(this);
        map.setOnMarkerClickListener(this);
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
    public void showPins(List<DepositionPoint> pins) {
        if (clusterManager == null)
            return;
        clusterManager.clearItems();

        Log.d("MapFragment", "adding pins: " + pins.size());
        List<DepositionPointClusterItem> items = new ArrayList<>();

        for (DepositionPoint pin : pins) {
            items.add(new DepositionPointClusterItem(
                    pin.getLatitude(),
                    pin.getLongitude(),
                    pin.getPartnerName()));
        }

        clusterManager.addItems(items);
        clusterManager.cluster();
        if (depositionPointsChangedListener != null)
            depositionPointsChangedListener.onChangeDepositionPoints(pins, currentUserLocation);
    }

    @Override
    public void showBottomSheet(DepositionPoint point, String name, String iconUrl) {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        PointDetailBottomSheet.newInstance(point, name, iconUrl)
                .setClickListener(this::onBottomSheetClick)
                .show(fragmentManager, "PointDetailBottomSheet_" + name);
    }

    @Override
    public void moveToPoint(MapCoordinates coordinates) {
        if (map != null)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(GoogleMapUtils.toLatLng(coordinates), 15));
    }

    @Override
    public void showSnackbar(String message) {
        View view = getView();
        if (view != null)
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    private void onBottomSheetClick(DepositionPoint point, View iconView) {
        DepositionPointDetailActivity.start(
                getActivity(),
                point,
                currentUserLocation,
                iconView);
    }

    @Override
    public void onCameraIdle() {
        Log.d("MapFragment", "onCameraIdle");
        clusterManager.onCameraIdle();
        cameraStops();
    }

    @Override
    public void onCameraMoveCanceled() {
        Log.d("MapFragment", "onCameraMoveCanceled");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        depositionPointsChangedListener = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        presenter.clickOnMarker(marker.getPosition().latitude, marker.getPosition().longitude);
        return clusterManager.onMarkerClick(marker);
    }

    @Override
    public void onMyLocationChange(Location location) {
        currentUserLocation = GoogleMapUtils.toCoordinates(location);
        if (firstLaunch) {
            moveToPoint(GoogleMapUtils.toCoordinates(location));
            firstLaunch = false;
        }
    }
}
