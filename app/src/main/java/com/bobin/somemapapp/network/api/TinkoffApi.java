package com.bobin.somemapapp.network.api;

import com.bobin.somemapapp.model.response.DepositionPointsResponse;
import com.bobin.somemapapp.model.response.DepositionPartnersResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TinkoffApi {
    @GET("deposition_partners")
    Single<DepositionPartnersResponse> getDepositionPartners(@Query("accountType") String accountType);

    @GET("deposition_points")
    Single<DepositionPointsResponse> getDepositionPoints(@Query("latitude") double latitude,
                                                             @Query("longitude") double longitude,
                                                             @Query("partners") String partners,
                                                             @Query("radius") int radius);

    @GET("deposition_points")
    Single<DepositionPointsResponse> getDepositionPoints(@Query("latitude") double latitude,
                                                             @Query("longitude") double longitude,
                                                             @Query("radius") int radius);
}
