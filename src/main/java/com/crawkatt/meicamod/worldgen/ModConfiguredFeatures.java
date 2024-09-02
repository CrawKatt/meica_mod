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
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;
import java.util.OptionalInt;

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

        register(context, BIG_OAK_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.OAK_LOG), // Define el tronco del árbol
                // (12 Altura base del tronco)
                // (4 Variación aleatoria en la altura del tronco { puede agregar hasta 4 bloques a la altura base } )
                // (8 Longitud del tronco antes de ramificarse)
                new DarkOakTrunkPlacer(6, 4, 8), // Define como se debe generar el tronco del árbol (Trunk Placer)
                BlockStateProvider.simple(Blocks.OAK_LEAVES), // Define el tipo de hojas del árbol
                // (0 Radio y Desplazamiento vertical. Las Hojas estarán compactas alrededor del tronco, sin extensión adicional)
                new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of( 0)), // Define como se deben generar las hojas del árbol (Foliage Placer)
                // (1,1 Dimensiones de la primera y segunda capa )
                // (0 Altura de la tercera capa { Sin tercera capa } )
                // (1,2 Los tamaños adicionales en las capas superiores)
                // (OptionalInt.empty() No hay restricción máxima de altura adicional)
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()) // Define el tamaño y la forma del árbol
        ).build());

        // Generación de geodas de brotenita
        register(context, BROTENITA_GEODE_KEY, Feature.GEODE,
                new GeodeConfiguration(
                        new GeodeBlockSettings(
                                // (AIR Bloque de relleno)
                                BlockStateProvider.simple(Blocks.AIR),

                                // Capa de llenado del núcleo de la geoda (usualmente la capa más interna de roca)
                                BlockStateProvider.simple(Blocks.DEEPSLATE),

                                // Capa de material interno (el bloque personalizado RAW_BROTENITA_BLOCK)
                                BlockStateProvider.simple(ModBlocks.RAW_BROTENITA_BLOCK.get()),

                                // Capa intermedia
                                BlockStateProvider.simple(Blocks.MOSS_BLOCK),

                                // Capa de la corteza externa (bloques de "deepslate" para la capa externa)
                                BlockStateProvider.simple(Blocks.DEEPSLATE),

                                // Lista de posibles bloques que aparecerán como cristales en la geoda
                                List.of(ModBlocks.RAW_BROTENITA.get().defaultBlockState()),

                                // Bloques que no pueden ser reemplazados durante la generación de la geoda
                                BlockTags.FEATURES_CANNOT_REPLACE,

                                // Bloques inválidos para la generación de la geoda
                                BlockTags.GEODE_INVALID_BLOCKS
                        ),
                        new GeodeLayerSettings(1.7D, 1.2D, 2.5D, 3.5D),
                        new GeodeCrackSettings(0.25D, 1.5D, 1), 0.5D, 0.1D,
                        true, UniformInt.of(3, 8),
                        UniformInt.of(2, 6), UniformInt.of(1, 2),
                        -18, 18, 0.075D, 1
                )
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(MeicaMod.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
