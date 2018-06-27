package com.bobin.somemapapp.ui.view;

import com.arellomobile.mvp.MvpView;
import com.bobin.somemapapp.model.tables.DepositionPartner;

public interface DepositionPointDetailView extends MvpView {
    void showDistance(int meters);
    void showPartner(DepositionPartner partner);
    void finishActivity();
}
