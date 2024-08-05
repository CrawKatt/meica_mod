package com.crawkatt.meicamod.worldgen.biome;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Regions;

public class ModTerrablender {
    public static void registerBiomes() {
        Regions.register(new ModOverworldRegion(new ResourceLocation(MeicaMod.MODID, "overworld"), 5));
    }
}