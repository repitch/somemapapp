package com.bobin.somemapapp.infrastructure;

import android.content.Context;

import com.bobin.somemapapp.R;

import java.net.UnknownHostException;

public class ExceptionsHandler {
    private Context context;
    private long lastNetworkMessage;

    public ExceptionsHandler(Context context) {
        this.context = context;
    }

    public String getSnackbarMessage(Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            long now = System.currentTimeMillis();
            if (now - lastNetworkMessage > 1000 * 60 /* 1 minute */) {
                lastNetworkMessage = now;
                return context.getString(R.string.no_internet);
            }
            return null;
        }
        return context.getString(R.string.unknown_error);
    }
}
