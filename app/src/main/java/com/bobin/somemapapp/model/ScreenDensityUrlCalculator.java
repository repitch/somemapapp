package com.bobin.somemapapp.model;

import android.content.Context;

import com.bobin.somemapapp.BuildConfig;
import com.bobin.somemapapp.model.tables.DepositionPartner;

public class ScreenDensityUrlCalculator {
    private float value;

    public ScreenDensityUrlCalculator(Context context) {
        this.value = context.getResources().getDisplayMetrics().density;
    }

    public float getValue() {
        return value;
    }

    public String getStringValue() {
        if (value <= 1)
            return "mdpi";
        if (value <= 1.5f)
            return "hdpi";
        if (value <= 2.0f)
            return "xhdpi";
        return "xxhdpi";
    }

    public String getPartnerPictureUrl(DepositionPartner partner) {
        return BuildConfig.ICONS_ENDPOINT + getStringValue() + "/" + partner.getPicture();
    }
}
