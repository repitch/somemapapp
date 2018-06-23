package com.bobin.somemapapp.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TinkoffApiResponse<T extends Serializable> implements Serializable {
    @SerializedName("resultCode")
    @Expose
    private String resultCode;
    @SerializedName("trackingId")
    @Expose
    private String trackingId;
    @SerializedName("payload")
    @Expose
    private List<T> payload;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public List<T> getPayload() {
        return payload;
    }

    public void setPayload(List<T> payload) {
        this.payload = payload;
    }
}