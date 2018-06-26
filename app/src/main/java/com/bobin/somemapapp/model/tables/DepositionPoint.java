package com.bobin.somemapapp.model.tables;

import com.bobin.somemapapp.model.MapCoordinates;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DepositionPoint extends RealmObject {
    @PrimaryKey
    private String externalId;
    private String partnerName;
    private double latitude;
    private double longitude;
    private String workHours;
    private String fullAddress;

    public MapCoordinates getMapCoordinates() {
        return new MapCoordinates(latitude, longitude);
    }

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    @Override
    public String toString() {
        return getPartnerName() + " " + getFullAddress();
    }
}
