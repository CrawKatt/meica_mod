package com.example.examplemod.worldgen.biome;

import com.example.examplemod.ExampleMod;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Regions;

public class ModTerrablender {
    public static void registerBiomes() {
        Regions.register(new ModOverworldRegion(new ResourceLocation(ExampleMod.MODID, "overworld"), 5));
    }
}