package com.example.examplemod.worldgen.biome.custom;

import org.joml.Vector3f;

public class FogProfile {
    private Vector3f rgb;
    private float fogStart;
    private float fogEnd;

    public FogProfile(Vector3f rgb, float fogStart, float fogEnd) {
        this.rgb = rgb;
        this.fogStart = fogStart;
        this.fogEnd = fogEnd;
    }

    public Vector3f getRgb() {
        return rgb;
    }

    public void setRgb(Vector3f rgb) {
        this.rgb = rgb;
    }

    public float getFogStart() {
        return fogStart;
    }

    public void setFogStart(float fogStart) {
        this.fogStart = fogStart;
    }

    public float getFogEnd() {
        return fogEnd;
    }

    public void setFogEnd(float fogEnd) {
        this.fogEnd = fogEnd;
    }
}
