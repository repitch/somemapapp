package com.bobin.somemapapp.model.tables;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DepositionPartner extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;
    private String picture;
    private String url;
    private Boolean hasLocations;
    private Boolean isMomentary;
    private String depositionDuration;
    private String limitations;
    private String pointType;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getHasLocations() {
        return hasLocations;
    }

    public void setHasLocations(Boolean hasLocations) {
        this.hasLocations = hasLocations;
    }

    public Boolean getMomentary() {
        return isMomentary;
    }

    public void setMomentary(Boolean momentary) {
        isMomentary = momentary;
    }

    public String getDepositionDuration() {
        return depositionDuration;
    }

    public void setDepositionDuration(String depositionDuration) {
        this.depositionDuration = depositionDuration;
    }

    public String getLimitations() {
        return limitations;
    }

    public void setLimitations(String limitations) {
        this.limitations = limitations;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
