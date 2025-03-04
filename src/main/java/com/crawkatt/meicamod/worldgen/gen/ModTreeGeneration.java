package com.crawkatt.meicamod.worldgen.gen;

import com.crawkatt.meicamod.worldgen.ModPlacedFeatures;
import com.crawkatt.meicamod.worldgen.biome.ModBiomes;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public class ModTreeGeneration {
    public static void generateTrees() {
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.MEICA_FOREST),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.BIG_OAK_PLACED_KEY);
    }
}
