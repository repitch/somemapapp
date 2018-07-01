package com.bobin.somemapapp.helper;

import com.bobin.somemapapp.infrastructure.Clock;
import com.bobin.somemapapp.model.response.DepositionPartnerResponse;
import com.bobin.somemapapp.model.response.DepositionPartnersResponse;
import com.bobin.somemapapp.model.response.DepositionPointResponse;
import com.bobin.somemapapp.model.response.DepositionPointsResponse;
import com.bobin.somemapapp.model.response.LocationResponse;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.model.tables.PointsCircle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class DataGenerator {
    private DataGenerator() {
    }

    public static List<String> ids(DepositionPartnersResponse partnersResponse, int take) {
        List<DepositionPartnerResponse> payload = partnersResponse.getPayload();
        List<String> result = new ArrayList<>(take);
        for (int i = 0; i < take; ++i)
            result.add(payload.get(i).getId());

        return result;
    }

    public static String[] ids(List<DepositionPartner> partners) {
        String[] result = new String[partners.size()];
        for (int i = 0; i < partners.size(); ++i) {
            result[i] = partners.get(i).getId();
        }
        return result;
    }

    public static <T> List<T> asList(T... a) {
        return Arrays.asList(a);
    }

    public static List<DepositionPoint> generatePoints(int count) {
        List<DepositionPoint> result = new ArrayList<>(count);

        for (int i = 0; i < count; ++i)
            result.add(generateDepositionPoint());

        return result;
    }

    public static List<DepositionPartner> generatePartners(int count) {
        List<DepositionPartner> result = new ArrayList<>(count);

        for (int i = 0; i < count; ++i)
            result.add(generatePartner());

        return result;
    }

    public static List<DepositionPoint> convert(DepositionPointsResponse response) {
        List<DepositionPoint> result = new ArrayList<>();
        for (DepositionPointResponse pointResponse : response.getPayload()) {
            DepositionPoint partner = new DepositionPoint();
            partner.setExternalId(pointResponse.getExternalId());
            result.add(partner);
        }
        return result;
    }

    public static List<DepositionPartner> convert(DepositionPartnersResponse response) {
        List<DepositionPartner> result = new ArrayList<>();
        for (DepositionPartnerResponse partnerResponse : response.getPayload()) {
            DepositionPartner partner = new DepositionPartner();
            partner.setId(partnerResponse.getId());
            result.add(partner);
        }
        return result;
    }

    public static DepositionPointsResponse generatePointsResponse() {
        DepositionPointResponse pointResponse = new DepositionPointResponse();
        pointResponse.setExternalId(randomString());
        LocationResponse location = new LocationResponse();
        location.setLatitude(42d);
        location.setLongitude(36d);
        pointResponse.setLocation(location);
        DepositionPointsResponse response = new DepositionPointsResponse();
        response.setPayload(asList(pointResponse));
        return response;
    }

    public static DepositionPartnersResponse generatePartnersResponse(int count) {
        List<DepositionPartnerResponse> partners = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            DepositionPartnerResponse partnerResponse = new DepositionPartnerResponse();
            partnerResponse.setLimits(new ArrayList<>());
            partnerResponse.setId(randomString());
            partners.add(partnerResponse);
        }

        DepositionPartnersResponse response = new DepositionPartnersResponse();
        response.setPayload(partners);
        return response;
    }

    public static DepositionPoint generateDepositionPoint() {
        DepositionPoint point = new DepositionPoint();
        point.setExternalId(randomString());
        return point;
    }

    public static DepositionPartner generatePartner() {
        DepositionPartner depositionPartner = new DepositionPartner();
        depositionPartner.setId(randomString());
        return depositionPartner;
    }

    public static String randomString() {
        return randomString(10);
    }

    public static String randomString(int length) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] chars = new char[length];
        Random random = new Random();
        for (int i = 0; i < length; ++i)
            chars[i] = upper.charAt(random.nextInt(upper.length()));
        return new String(chars);
    }

    public static PointsCircle circleAt(double lat, double lng, int radius, Clock clock) {
        return new PointsCircle(lat, lng, radius, clock);
    }

    public static DepositionPoint depositionPointAt(double lat, double lng) {
        DepositionPoint depositionPoint = new DepositionPoint();
        depositionPoint.setLatitude(lat);
        depositionPoint.setLongitude(lng);
        depositionPoint.setExternalId(randomString());
        return depositionPoint;
    }
}
