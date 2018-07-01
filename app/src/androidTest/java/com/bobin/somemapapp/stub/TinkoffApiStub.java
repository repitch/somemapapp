package com.bobin.somemapapp.stub;

import com.bobin.somemapapp.model.response.DepositionPartnersResponse;
import com.bobin.somemapapp.model.response.DepositionPointsResponse;
import com.bobin.somemapapp.network.api.TinkoffApi;

import io.reactivex.Single;

public class TinkoffApiStub implements TinkoffApi {
    @Override
    public Single<DepositionPartnersResponse> getDepositionPartners(String accountType) {
        return null;
    }

    @Override
    public Single<DepositionPointsResponse> getDepositionPoints(double latitude, double longitude, String partners, int radius) {
        return null;
    }

    @Override
    public Single<DepositionPointsResponse> getDepositionPoints(double latitude, double longitude, int radius) {
        return null;
    }
}
