package com.bobin.somemapapp.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DepositionPartnerResponse implements Serializable {

    @SerializedName("id")
    @Expose // не обязательно у всех полей писать @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("hasLocations")
    @Expose
    private Boolean hasLocations;
    @SerializedName("isMomentary")
    @Expose
    private Boolean isMomentary;
    @SerializedName("depositionDuration")
    @Expose
    private String depositionDuration;
    @SerializedName("limitations")
    @Expose
    private String limitations;
    @SerializedName("pointType")
    @Expose
    private String pointType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("limits")
    @Expose
    private List<LimitResponse> limits;

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

    public Boolean getIsMomentary() {
        return isMomentary;
    }

    public void setIsMomentary(Boolean isMomentary) {
        this.isMomentary = isMomentary;
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

    public List<LimitResponse> getLimits() {
        return limits;
    }

    public void setLimits(List<LimitResponse> limits) {
        this.limits = limits;
    }
}
