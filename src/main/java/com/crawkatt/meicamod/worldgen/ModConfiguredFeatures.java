package com.crawkatt.meicamod.worldgen;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.foliage.DarkOakFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;

import java.util.List;
import java.util.OptionalInt;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> OVERWORLD_BROTENITA_ORE_KEY = registerKey("brotenita_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_OAK_KEY = registerKey("big_oak");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BROTENITA_GEODE_KEY = registerKey("brotenita_geode");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreFeatureConfig.Target> overworldBrotenitaOre = List.of(OreFeatureConfig.createTarget(stoneReplaceable,
                        ModBlocks.RAW_BROTENITA_BLOCK.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateReplaceables, ModBlocks.RAW_BROTENITA_BLOCK.getDefaultState()));

        register(context, OVERWORLD_BROTENITA_ORE_KEY, Feature.ORE, new OreFeatureConfig(overworldBrotenitaOre, 9));

        register(context, BIG_OAK_KEY, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(Blocks.OAK_LOG), // Define el tronco del árbol
                // (12 Altura base del tronco)
                // (4 Variación aleatoria en la altura del tronco { puede agregar hasta 4 bloques a la altura base } )
                // (8 Longitud del tronco antes de ramificarse)
                new DarkOakTrunkPlacer(6, 4, 8), // Define como se debe generar el tronco del árbol (Trunk Placer)
                BlockStateProvider.of(Blocks.OAK_LEAVES), // Define el tipo de hojas del árbol
                // (0 Radio y Desplazamiento vertical. Las Hojas estarán compactas alrededor del tronco, sin extensión adicional)
                new DarkOakFoliagePlacer(ConstantIntProvider.create(0), ConstantIntProvider.create(0)), // Define como se deben generar las hojas del árbol (Foliage Placer)
                // (1,1 Dimensiones de la primera y segunda capa )
                // (0 Altura de la tercera capa { Sin tercera capa } )
                // (1,2 Los tamaños adicionales en las capas superiores)
                // (OptionalInt.empty() No hay restricción máxima de altura adicional)
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()) // Define el tamaño y la forma del árbol
        ).build());

        // Generación de geodas de brotenita
        register(context, BROTENITA_GEODE_KEY, Feature.GEODE,
                new GeodeFeatureConfig(
                        new GeodeLayerConfig(
                                // (AIR Bloque de relleno)
                                BlockStateProvider.of(Blocks.AIR),

                                // Capa de llenado del núcleo de la geoda (usualmente la capa más interna de roca)
                                BlockStateProvider.of(Blocks.DEEPSLATE),

                                // Capa de material interno (el bloque personalizado RAW_BROTENITA_BLOCK)
                                BlockStateProvider.of(ModBlocks.RAW_BROTENITA_BLOCK),

                                // Capa intermedia
                                BlockStateProvider.of(Blocks.MOSS_BLOCK),

                                // Capa de la corteza externa (bloques de "deepslate" para la capa externa)
                                BlockStateProvider.of(Blocks.DEEPSLATE),

                                // Lista de posibles bloques que aparecerán como cristales en la geoda
                                List.of(ModBlocks.BROTENITA.getDefaultState()),

                                // Bloques que no pueden ser reemplazados durante la generación de la geoda
                                BlockTags.FEATURES_CANNOT_REPLACE,

                                // Bloques inválidos para la generación de la geoda
                                BlockTags.GEODE_INVALID_BLOCKS
                        ),
                        new GeodeLayerThicknessConfig(1.7D, 1.2D, 2.5D, 3.5D),
                        new GeodeCrackConfig(0.25D, 1.5D, 1), 0.5D, 0.1D,
                        true, UniformIntProvider.create(3, 8),
                        UniformIntProvider.create(2, 6), UniformIntProvider.create(1, 2),
                        -18, 18, 0.075D, 1
                )
        );
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(MeicaMod.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
