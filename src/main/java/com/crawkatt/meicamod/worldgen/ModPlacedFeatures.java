package com.crawkatt.meicamod.worldgen;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> BROTENITA_ORE_PLACED_KEY = registerKey("brotenita_ore_placed");
    public static final ResourceKey<PlacedFeature> BIG_OAK_PLACED_KEY = registerKey("big_oak_placed");
    public static final ResourceKey<PlacedFeature> BROTENITA_GEODE_PLACED_KEY = registerKey("brotenita_geode_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, BROTENITA_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_BROTENITA_ORE_KEY),
                ModOrePlacement.rareOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, BIG_OAK_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BIG_OAK_KEY),
                List.of(
                        PlacementUtils.countExtra(2, 0.1f, 2),
                        InSquarePlacement.spread(),
                        SurfaceWaterDepthFilter.forMaxDepth(0), // Evita que se genere en agua
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                )
        );

        register(context, BROTENITA_GEODE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BROTENITA_GEODE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(50), InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(50)),
                        BiomeFilter.biome()));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(MeicaMod.MODID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
