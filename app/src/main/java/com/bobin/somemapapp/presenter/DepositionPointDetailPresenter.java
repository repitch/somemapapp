package com.bobin.somemapapp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.ui.view.DepositionPointDetailView;

@InjectViewState
public class DepositionPointDetailPresenter extends MvpPresenter<DepositionPointDetailView> {
    public void onStart(String partnerId, MapCoordinates userPosition) {
        if (userPosition != null) {

        }
    }

    private void calculateDistance(DepositionPartner partner, MapCoordinates userPosition) {
        if (partner == null || userPosition == null)
            return;
        partner.
    }
}
