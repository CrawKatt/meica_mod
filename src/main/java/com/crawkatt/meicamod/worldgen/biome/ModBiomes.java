package com.crawkatt.meicamod.worldgen.biome;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.ModEntities;
import net.minecraft.client.sound.MusicType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;


public class ModBiomes {
    public static final RegistryKey<Biome> MEICA_FOREST = RegistryKey.of(RegistryKeys.BIOME,
            new Identifier(MeicaMod.MOD_ID, "meica_forest"));

    public static final RegistryKey<Biome> MEICADIM_FOREST = RegistryKey.of(RegistryKeys.BIOME,
            new Identifier(MeicaMod.MOD_ID, "meicadim_forest"));

    public static void boostrap(Registerable<Biome> context) {
        context.register(MEICA_FOREST, meicaForestBiome(context));
        context.register(MEICADIM_FOREST, meicaForestDimBiome(context));
    }

    public static void globalOverworldGeneration(GenerationSettings.LookupBackedBuilder builder) {
        // Carvernas y lagos
        builder.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CAVE);
        builder.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CAVE_EXTRA_UNDERGROUND);
        builder.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);

        // Mantener lagos de agua en la superficie si es necesario
        builder.feature(GenerationStep.Feature.LAKES, MiscPlacedFeatures.SPRING_WATER);

        // Mantener solo lagos de lava subterráneos
        builder.feature(GenerationStep.Feature.LAKES, MiscPlacedFeatures.LAKE_LAVA_UNDERGROUND);

        // Formaciones subterráneas
        DefaultBiomeFeatures.addDungeons(builder);
        DefaultBiomeFeatures.addMineables(builder);

        // Manantiales
        DefaultBiomeFeatures.addSprings(builder);

        // Estructuras superficiales
        DefaultBiomeFeatures.addFrozenTopLayer(builder);
    }

    public static void globalDimGeneration(GenerationSettings.LookupBackedBuilder builder) {
        // Mantener lagos de agua en la superficie si es necesario
        builder.feature(GenerationStep.Feature.LAKES, MiscPlacedFeatures.SPRING_WATER);

        // Formaciones subterráneas
        DefaultBiomeFeatures.addDungeons(builder);
        DefaultBiomeFeatures.addMineables(builder);

        // Manantiales
        builder.feature(GenerationStep.Feature.FLUID_SPRINGS, MiscPlacedFeatures.SPRING_WATER);

        // Estructuras superficiales
        DefaultBiomeFeatures.addFrozenTopLayer(builder);
    }

    public static Biome meicaForestBiome(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        spawnBuilder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(ModEntities.BROTECITO, 5, 4, 4));

        // Spawn de Mobs
        DefaultBiomeFeatures.addFarmAnimals(spawnBuilder);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnBuilder);

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE), context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        //we need to follow the same order as vanilla biomes for the BiomeDefaultFeatures
        // Cavernas y Lagos
        globalOverworldGeneration(biomeBuilder);

        // Vegetación base
        DefaultBiomeFeatures.addForestFlowers(biomeBuilder);
        DefaultBiomeFeatures.addPlainsFeatures(biomeBuilder);
        DefaultBiomeFeatures.addDefaultOres(biomeBuilder);
        DefaultBiomeFeatures.addDefaultFlowers(biomeBuilder);

        // Vegetación extra
        DefaultBiomeFeatures.addDefaultVegetation(biomeBuilder);

        return new Biome.Builder()
                .precipitation(true)
                .downfall(0.8f)
                .temperature(0.7f)
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects(new BiomeEffects.Builder()
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533)
                        .skyColor(0x77ADFF)
                        .fogColor(0x0b6623)
                        .particleConfig(new BiomeParticleConfig(ParticleTypes.SPORE_BLOSSOM_AIR, 0.05f))
                        .music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_WARPED_FOREST))
                        .moodSound(BiomeMoodSound.CAVE).build())
                .build();
    }

    public static Biome meicaForestDimBiome(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        spawnBuilder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(ModEntities.BROTECITO, 5, 4, 4));

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE), context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        //we need to follow the same order as vanilla biomes for the BiomeDefaultFeatures
        // Cavernas y Lagos
        globalDimGeneration(biomeBuilder);

        // Vegetación base
        DefaultBiomeFeatures.addSweetBerryBushes(biomeBuilder);
        DefaultBiomeFeatures.addForestFlowers(biomeBuilder);
        DefaultBiomeFeatures.addPlainsFeatures(biomeBuilder);
        DefaultBiomeFeatures.addDefaultOres(biomeBuilder);
        DefaultBiomeFeatures.addDefaultFlowers(biomeBuilder);

        // Vegetación extra
        DefaultBiomeFeatures.addDefaultVegetation(biomeBuilder);

        return new Biome.Builder()
                .precipitation(true)
                .downfall(0.8f)
                .temperature(0.7f)
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects(new BiomeEffects.Builder()
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533)
                        .skyColor(0x77ADFF)
                        .fogColor(0x0b6623)
                        .particleConfig(new BiomeParticleConfig(ParticleTypes.SPORE_BLOSSOM_AIR, 0.05f))
                        .music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_WARPED_FOREST))
                        .moodSound(BiomeMoodSound.CAVE).build())
                .build();
    }
}
