package com.crawkatt.meicamod.worldgen;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class ModPlacedFeatures {
    public static final RegistryKey<PlacedFeature> BIG_OAK_PLACED_KEY = registerKey("big_oak_placed");
    public static final RegistryKey<PlacedFeature> FALLEN_HOLLOW_LOG_PLACED_KEY = registerKey("fallen_hollow_log_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> configuredFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, BIG_OAK_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BIG_OAK_KEY),
                CountPlacementModifier.of(16),
                SquarePlacementModifier.of(), // Distribución en cuadrícula uniforme dentro de un chunk
                SurfaceWaterDepthFilterPlacementModifier.of(0), // Evita que se genere en agua
                PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, // Genera los árboles en la tierra
                PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING), // Esto arregla el problema de los árboles flotantes
                BiomePlacementModifier.of()
        );

        register(context, FALLEN_HOLLOW_LOG_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.FALLEN_HOLLOW_LOG_KEY),
                CountPlacementModifier.of(1),
                SquarePlacementModifier.of(),
                BiomePlacementModifier.of(),
                PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
                SurfaceWaterDepthFilterPlacementModifier.of(0),
                PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING)
        );
    }

    private static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(MeicaMod.MOD_ID, name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key,
                                                                                   RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                                                                   PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }
}
