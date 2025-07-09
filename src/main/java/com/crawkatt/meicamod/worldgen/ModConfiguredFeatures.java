package com.crawkatt.meicamod.worldgen;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.worldgen.tree.custom.HollowOakTrunkPlacer;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.DarkOakFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;

import java.util.OptionalInt;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_OAK_KEY = registerKey("big_oak");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FALLEN_HOLLOW_LOG_KEY = registerKey("fallen_hollow_log");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {

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

        register(context, FALLEN_HOLLOW_LOG_KEY, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.HOLLOW_OAK_LOG),
                new HollowOakTrunkPlacer(2, 1, 1),
                BlockStateProvider.of(Blocks.AIR),
                new BlobFoliagePlacer(ConstantIntProvider.create(0), ConstantIntProvider.create(0), 0),
                new TwoLayersFeatureSize(1, 0, 0)).dirtProvider(BlockStateProvider.of(Blocks.DIRT)).build());
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(MeicaMod.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
