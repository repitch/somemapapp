package com.bobin.somemapapp.infrastructure;

import android.content.Context;
import android.util.Log;

import com.bobin.somemapapp.R;

import java.net.UnknownHostException;

public class ExceptionsHandler {
    private Context context;
    private Clock clock;
    private long lastNetworkMessage;

    public ExceptionsHandler(Context context, Clock clock) {
        this.context = context;
        this.clock = clock;
    }

    public String getSnackbarMessage(Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            long now = clock.currentTimeInMillis();
            if (now - lastNetworkMessage > 1000 * 60 /* 1 minute */) {
                lastNetworkMessage = now;
                return context.getString(R.string.no_internet); // для отсутствующего интернет-соединения есть нормальный способ проверки (слушать BroadCast)
            }
            return null;
        }

        logError(throwable);
        return null;
    }

    private void logError(Throwable throwable) {
        Log.e("ExceptionsHandler", "not handled error", throwable);
    }
}
