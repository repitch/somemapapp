package com.bobin.somemapapp.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DepositionPointResponse implements Serializable {

    @SerializedName("externalId")
    @Expose
    private String externalId;
    @SerializedName("partnerName")
    @Expose
    private String partnerName;
    @SerializedName("location")
    @Expose
    private LocationResponse location;
    @SerializedName("workHours")
    @Expose
    private String workHours;
    @SerializedName("fullAddress")
    @Expose
    private String fullAddress;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse location) {
        this.location = location;
    }

    public String getWorkHours() {
        return workHours;
    }

    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}