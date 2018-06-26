package com.bobin.somemapapp.utils;

import com.bobin.somemapapp.model.response.DepositionPartnerResponse;
import com.bobin.somemapapp.model.response.DepositionPointResponse;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.model.tables.DepositionPoint;

public final class StorageUtils {
    public static DepositionPoint convert(DepositionPointResponse response) {
        DepositionPoint depositionPoint = new DepositionPoint();
        depositionPoint.setExternalId(response.getExternalId());
        depositionPoint.setLatitude(response.getLocation().getLatitude());
        depositionPoint.setLongitude(response.getLocation().getLongitude());
        depositionPoint.setFullAddress(response.getFullAddress());
        depositionPoint.setPartnerName(response.getPartnerName());
        depositionPoint.setWorkHours(response.getWorkHours());
        return depositionPoint;
    }

    public static DepositionPartner convert(DepositionPartnerResponse response) {
        DepositionPartner depositionPartner = new DepositionPartner();
        depositionPartner.setId(response.getId());
        depositionPartner.setName(response.getName());
        depositionPartner.setPicture(response.getPicture());
        depositionPartner.setUrl(response.getUrl());
        depositionPartner.setHasLocations(response.getHasLocations());
        depositionPartner.setMomentary(response.getIsMomentary());
        depositionPartner.setDepositionDuration(response.getDepositionDuration());
        depositionPartner.setLimitations(response.getLimitations());
        depositionPartner.setPointType(response.getPointType());
        depositionPartner.setDescription(response.getDescription());
        return depositionPartner;
    }
}
