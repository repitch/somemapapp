package com.bobin.somemapapp.helper;

import com.bobin.somemapapp.model.tables.DepositionPartner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class DataGenerator {
    private DataGenerator() {
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

    public static List<DepositionPartner> generatePartners(int count) {
        List<DepositionPartner> result = new ArrayList<>(count);

        for (int i = 0; i < count; ++i)
            result.add(generatePartner());

        return result;
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
}
