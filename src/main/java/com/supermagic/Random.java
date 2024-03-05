package com.supermagic;

import java.util.List;

public class Random {

    public static int random(int sum) {
        return (int) (Math.random() * sum);
    }

    public static <T> T random(List<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return collection.get(random(collection.size()));
    }
}
