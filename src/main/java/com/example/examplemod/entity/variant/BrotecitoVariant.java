package com.example.examplemod.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BrotecitoVariant {
    DEFAULT(0),
    DARK(1);

    private static final BrotecitoVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator
            .comparingInt(BrotecitoVariant::getId)).toArray(BrotecitoVariant[]::new);
    private final int id;

    BrotecitoVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BrotecitoVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}