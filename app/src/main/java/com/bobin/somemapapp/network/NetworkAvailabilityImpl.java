package com.bobin.somemapapp.network;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkAvailabilityImpl implements NetworkAvailability {
    private ConnectivityManager connectivityManager;

    public NetworkAvailabilityImpl(Context context) {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    @Override
    public boolean isAvailable() {
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
