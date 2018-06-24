package com.bobin.somemapapp.utils;

import com.bobin.somemapapp.model.response.DepositionPointResponse;
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
}
