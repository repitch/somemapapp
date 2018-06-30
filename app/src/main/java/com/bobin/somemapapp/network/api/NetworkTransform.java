package com.bobin.somemapapp.network.api;

import com.bobin.somemapapp.network.NetworkAvailability;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

public class NetworkTransform<T> implements ObservableTransformer<T, T> {
    private NetworkAvailability networkAvailability;

    public NetworkTransform(NetworkAvailability networkAvailability) {
        this.networkAvailability = networkAvailability;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        if (networkAvailability.isAvailable())
            return upstream;
        return Observable.empty();
    }
}
