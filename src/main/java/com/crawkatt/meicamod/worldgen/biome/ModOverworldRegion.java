package com.crawkatt.meicamod.worldgen.biome;

import com.crawkatt.meicamod.MeicaMod;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.worldgen.RegionUtils;

import java.util.List;
import java.util.function.Consumer;

public class ModOverworldRegion extends Region {
    public ModOverworldRegion(Identifier name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {
        /*
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();
        new ParameterUtils.ParameterPointListBuilder()
            .temperature(Temperature.NEUTRAL)
            .humidity(Humidity.NEUTRAL)
            .continentalness(ParameterRange.of(-0.3F, -0.25F))
            .erosion(Erosion.EROSION_6)
            .depth(Depth.SURFACE)
            .weirdness(Weirdness.LOW_SLICE_VARIANT_ASCENDING)
            .offset(0.0F, 0.1F, 0.2F, 0.3F, 0.4F)
            .build().forEach(point -> builder.add(point, ModBiomes.MEICA_FOREST));

        builder.build().forEach(mapper);
        */

        this.addBiomeSimilar(mapper, BiomeKeys.MUSHROOM_FIELDS, ModBiomes.MEICA_FOREST);
        /*
        this.addModifiedVanillaOverworldBiomes(mapper, modifiedVanillaOverworldBuilder -> {
            modifiedVanillaOverworldBuilder.replaceBiome(BiomeKeys.MUSHROOM_FIELDS, ModBiomes.MEICA_FOREST);
        });
        */
    }
}
