package com.bobin.somemapapp.helper;

import com.bobin.somemapapp.utils.CollectionUtils;
import com.bobin.somemapapp.utils.Func1;

import java.util.List;

public class MyAsserts {
    public static <T> void assertAny(List<T> source, Func1<T, Boolean> predicate) {
        if (!CollectionUtils.any(source, predicate))
            throw new RuntimeException();
    }
}
