package com.bobin.somemapapp.stub;

import com.bobin.somemapapp.model.response.DepositionPartnersResponse;
import com.bobin.somemapapp.model.response.DepositionPointsResponse;
import com.bobin.somemapapp.network.api.TinkoffApi;

import io.reactivex.Single;

public class TinkoffApiStub implements TinkoffApi {
    private DepositionPartnersResponse nextPartnersResponse;
    private DepositionPointsResponse nextPointsResponse;
    private boolean calledPartners;
    private boolean calledPoints;

    @Override
    public Single<DepositionPartnersResponse> getDepositionPartners(String accountType) {
        calledPartners = true;
        return Single.just(nextPartnersResponse);
    }

    @Override
    public Single<DepositionPointsResponse> getDepositionPoints(double latitude, double longitude, int radius) {
        calledPoints = true;
        return Single.just(nextPointsResponse);
    }

    public void setNextResponse(DepositionPointsResponse nextResponse) {
        this.nextPointsResponse = nextResponse;
    }

    public void setNextResponse(DepositionPartnersResponse nextResponse) {
        this.nextPartnersResponse = nextResponse;
    }

    public boolean isCalledPartners() {
        return calledPartners;
    }

    public boolean isCalledPoints() {
        return calledPoints;
    }
}
