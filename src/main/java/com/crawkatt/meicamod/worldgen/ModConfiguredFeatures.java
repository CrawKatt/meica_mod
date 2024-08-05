package com.crawkatt.meicamod.worldgen;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaJungleFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_BROTENITA_ORE_KEY = registerKey("brotenita_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BIG_OAK_KEY = registerKey("big_oak");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BROTENITA_GEODE_KEY = registerKey("brotenita_geode");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldBrotenitaOre = List.of(OreConfiguration.target(stoneReplaceable,
                        ModBlocks.RAW_BROTENITA_BLOCK.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.RAW_BROTENITA_BLOCK.get().defaultBlockState()));

        register(context, OVERWORLD_BROTENITA_ORE_KEY, Feature.ORE, new OreConfiguration(overworldBrotenitaOre, 9));

        // Generación de árboles gigantes
        TreeConfiguration bigOakConfig = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.OAK_LOG),
                new MegaJungleTrunkPlacer(10, 4, 19),
                BlockStateProvider.simple(Blocks.OAK_LEAVES),
                new MegaJungleFoliagePlacer(ConstantInt.of(2), ConstantInt.of( 0), 2),
                new TwoLayersFeatureSize(1, 1, 2)
        ).build();

        register(context, BIG_OAK_KEY, Feature.TREE, bigOakConfig);

        // Generación de geodas de brotenita
        register(context, BROTENITA_GEODE_KEY, Feature.GEODE,
                new GeodeConfiguration(new GeodeBlockSettings(BlockStateProvider.simple(Blocks.AIR), // Interior de la Geoda
                        BlockStateProvider.simple(Blocks.DEEPSLATE), // Capa intermedia de la Geoda
                        BlockStateProvider.simple(ModBlocks.RAW_BROTENITA_BLOCK.get()), // Capa intermedia de la Geoda
                        BlockStateProvider.simple(Blocks.DIRT), // Capa intermedia de la Geoda
                        BlockStateProvider.simple(ModBlocks.BROTENITA_BLOCK.get()), // Capa exterior de la Geoda
                        List.of(ModBlocks.BROTENITA_BLOCK.get().defaultBlockState()), // Capa exterior de la Geoda
                        BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.GEODE_INVALID_BLOCKS),
                        new GeodeLayerSettings(1.7D, 1.2D, 2.5D, 3.5D),
                        new GeodeCrackSettings(0.25D, 1.5D, 1), 0.5D, 0.1D,
                        true, UniformInt.of(3, 8),
                        UniformInt.of(2, 6), UniformInt.of(1, 2),
                        -18, 18, 0.075D, 1));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(MeicaMod.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
