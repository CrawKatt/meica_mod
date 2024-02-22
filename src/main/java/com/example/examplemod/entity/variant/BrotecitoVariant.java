package com.example.examplemod.entity.variant;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

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

    public static BrotecitoVariant getRandomVariant() {
        int randomNum = new Random().nextInt(100);

        if (randomNum < 95) {
            return DEFAULT;
        } else {
            return DARK;
        }
    }
}