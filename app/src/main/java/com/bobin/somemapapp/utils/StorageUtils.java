package com.bobin.somemapapp.utils;

import com.bobin.somemapapp.model.response.CurrencyResponse;
import com.bobin.somemapapp.model.response.DepositionPartnerResponse;
import com.bobin.somemapapp.model.response.DepositionPointResponse;
import com.bobin.somemapapp.model.response.LimitResponse;
import com.bobin.somemapapp.model.tables.Currency;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.model.tables.Limit;

import java.util.ArrayList;
import java.util.List;

// Maybe лучше для gson добавить кастомный десериализатор?
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
        depositionPartner.setLimits(convert(response.getLimits()));
        return depositionPartner;
    }

    private static List<Limit> convert(List<LimitResponse> limitsResponse) {
        List<Limit> result = new ArrayList<>(limitsResponse.size());
        for (LimitResponse limitResponse : limitsResponse) {
            CurrencyResponse currencyResponse = limitResponse.getCurrency();
            Currency currency = new Currency();
            currency.setCode(currencyResponse.getCode());
            currency.setName(currencyResponse.getName());
            Limit limit = new Limit();
            limit.setCurrency(currency);
            limit.setAmount(limitResponse.getAmount());
            limit.setMax(limitResponse.getMax());
            limit.setMin(limitResponse.getMin());
            result.add(limit);
        }
        return result;
    }
}
