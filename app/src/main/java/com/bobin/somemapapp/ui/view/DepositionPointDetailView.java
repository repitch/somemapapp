package com.bobin.somemapapp.ui.view;

import android.net.Uri;

import com.arellomobile.mvp.MvpView;
import com.bobin.somemapapp.model.tables.DepositionPartner;

public interface DepositionPointDetailView extends MvpView {
    void showDistance(int meters);

    void showPartner(DepositionPartner partner, String icon);

    void finishActivity();

    void openBrowser(Uri uri);
}
