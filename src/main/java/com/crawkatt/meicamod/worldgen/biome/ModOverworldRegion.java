package com.crawkatt.meicamod.worldgen.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import terrablender.api.ParameterUtils;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;
import terrablender.api.ParameterUtils.*;

import java.util.function.Consumer;

public class ModOverworldRegion extends Region {
    public ModOverworldRegion(Identifier name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();
        new ParameterUtils.ParameterPointListBuilder()
                .temperature(Temperature.COOL, Temperature.NEUTRAL, Temperature.WARM)
                .humidity(Humidity.DRY, Humidity.NEUTRAL)
                .continentalness(MultiNoiseUtil.ParameterRange.of(-0.25F, -0.19F))
                .erosion(Erosion.EROSION_5, Erosion.EROSION_6)
                .depth(Depth.SURFACE, Depth.FLOOR)
                .weirdness(Weirdness.LOW_SLICE_VARIANT_ASCENDING)
                .offset(0.0F, 0.1F, 0.2F, 0.3F, 0.4F)
                .build().forEach(point -> builder.add(point, ModBiomes.MEICA_FOREST));

        builder.build().forEach(mapper);
    }
}
