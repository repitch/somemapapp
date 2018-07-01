package com.bobin.somemapapp.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {
    public static String[] toArray(List<String> strings) {
        String[] array = new String[strings.size()];
        return strings.toArray(array);
    }

    public static <T, R> List<R> map(Collection<T> source, Func1<T, R> mapper) {
        List<R> result = new ArrayList<>(source.size());
        for (T item : source)
            result.add(mapper.invoke(item));
        return result;
    }

    public static <T> List<T> filter(Collection<T> source, Func1<T, Boolean> predicate) {
        List<T> result = new ArrayList<>(source.size() / 2);
        for (T item : source)
            if (predicate.invoke(item))
                result.add(item);
        return result;
    }

    public static <T> boolean any(Collection<T> source, Func1<T, Boolean> predicate) {
        for (T item : source)
            if (predicate.invoke(item))
                return true;
        return false;
    }

    public static <T> boolean all(Collection<T> source, Func1<T, Boolean> predicate) {
        for (T item : source)
            if (!predicate.invoke(item))
                return false;
        return true;
    }
}
