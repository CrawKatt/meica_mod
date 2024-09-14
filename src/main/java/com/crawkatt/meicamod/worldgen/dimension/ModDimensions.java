package com.crawkatt.meicamod.worldgen.dimension;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.worldgen.biome.ModBiomes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;

import java.util.List;
import java.util.OptionalLong;

public class ModDimensions {
    public static final ResourceKey<LevelStem> MEICADIM_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(MeicaMod.MODID, "meicadim"));
    public static final ResourceKey<Level> MEICADIM_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(MeicaMod.MODID, "meicadim"));
    public static final ResourceKey<DimensionType> MEICA_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(MeicaMod.MODID, "meicadim_type"));

    public static void boostrapType(BootstapContext<DimensionType> context) {
        context.register(MEICA_DIM_TYPE, new DimensionType(
                OptionalLong.of(1200), // define la hora de la dimension (fija)
                false, // hasSkyLight
                false, // hasCeiling
                false, // ultrawarm
                false, // natural
                1.0D, // coordinateScale
                false, // bedWorks
                false, // respawnAnchorWorks
                0, // minY
                256, // height
                256, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.NETHER_EFFECTS, // effectsLocation
                0.5f, // ambientLight
                new DimensionType.MonsterSettings(true, false, ConstantInt.of(0), 0)));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(Pair.of(
                                Climate.parameters(
                                        0.0F,
                                        0.0F,
                                        -0.4F,
                                        -0.6F,
                                        0.3F,
                                        0.4F,
                                        0.1F
                                ),
                                biomeRegistry.getOrThrow(ModBiomes.MEICADIM_FOREST))
                        ))),
                noiseGenSettings.getOrThrow(ModNoiseGeneratorSettings.FLOATING_FOREST));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.MEICA_DIM_TYPE), noiseBasedChunkGenerator);
        context.register(MEICADIM_KEY, stem);
    }
}