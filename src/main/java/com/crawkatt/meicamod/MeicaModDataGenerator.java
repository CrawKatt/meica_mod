package com.crawkatt.meicamod;

import com.crawkatt.meicamod.datagen.*;
import com.crawkatt.meicamod.worldgen.ModConfiguredFeatures;
import com.crawkatt.meicamod.worldgen.ModPlacedFeatures;
import com.crawkatt.meicamod.worldgen.biome.ModBiomes;
import com.crawkatt.meicamod.worldgen.dimension.ModDimensions;
import com.crawkatt.meicamod.worldgen.dimension.ModNoiseGeneratorSettings;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class MeicaModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModWorldGenerator::new);
		pack.addProvider(ModAdvancementProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.BIOME, ModBiomes::boostrap);

        // No es posible añadir dimensiones al Datagen en Fabric. Se debe añadir meicadim.json manualmente
		registryBuilder.addRegistry(RegistryKeys.DIMENSION_TYPE, ModDimensions::boostrapType);
		registryBuilder.addRegistry(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModNoiseGeneratorSettings::bootstrap);
	}
}
