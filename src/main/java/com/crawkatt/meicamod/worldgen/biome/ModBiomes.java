package com.crawkatt.meicamod.worldgen.biome;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.worldgen.ModPlacedFeatures;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModBiomes {
    public static final ResourceKey<Biome> MEICA_FOREST = ResourceKey.create(Registries.BIOME,
            new ResourceLocation(MeicaMod.MODID, "meica_forest"));

    public static void boostrap(BootstapContext<Biome> context) {
        context.register(MEICA_FOREST, meicaForestBiome(context));
    }

    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        // Carvernas y lagos
        builder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
        builder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND);
        builder.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);

        // Mantener lagos de agua en la superficie si es necesario
        builder.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.SPRING_WATER);

        // Mantener solo lagos de lava subterráneos
        builder.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND);

        // Formaciones subterráneas
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);

        // Manantiales
        BiomeDefaultFeatures.addDefaultSprings(builder);

        // Estructuras superficiales
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    public static Biome meicaForestBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.BROTECITO.get(), 5, 4, 4));

        // Spawn de Mobs
        BiomeDefaultFeatures.farmAnimals(spawnBuilder);
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
        //we need to follow the same order as vanilla biomes for the BiomeDefaultFeatures
        // Cavernas y Lagos
        globalOverworldGeneration(biomeBuilder);

        // Generación de Árboles Gigantes (PRIORIZAR ANTES DE LO DEMÁS ABAJO)
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BIG_OAK_PLACED_KEY);

        // Vegetación base
        BiomeDefaultFeatures.addForestFlowers(biomeBuilder);
        BiomeDefaultFeatures.addPlainVegetation(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
        BiomeDefaultFeatures.addDefaultFlowers(biomeBuilder);

        // Vegetación extra
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomeBuilder);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.7f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533)
                        .skyColor(0x77ADFF)
                        .fogColor(0x0b6623)
                        .ambientParticle(new AmbientParticleSettings(ParticleTypes.FALLING_SPORE_BLOSSOM, 0.005f))
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build())
                .build();
    }
}