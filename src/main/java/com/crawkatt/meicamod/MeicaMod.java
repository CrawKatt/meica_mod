package com.crawkatt.meicamod;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.block.entity.ModBlockEntities;
import com.crawkatt.meicamod.component.ModComponents;
import com.crawkatt.meicamod.effect.ModEffects;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.item.ModItemGroups;
import com.crawkatt.meicamod.item.ModItems;
import com.crawkatt.meicamod.particle.ModParticles;
import com.crawkatt.meicamod.recipe.ModRecipes;
import com.crawkatt.meicamod.screen.ModScreenHandlers;
import com.crawkatt.meicamod.sound.ModSounds;
import com.crawkatt.meicamod.util.ModRegistries;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeicaMod implements ModInitializer, EntityComponentInitializer {
	public static final String MOD_ID = "meicamod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModItemGroups.registerItemGroups();
		ModRegistries.registerModStuffs();
		ModEffects.registerEffects();
		ModEntities.registerModEntities();
		ModSounds.registerSounds();
		ModParticles.registerParticles();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
		ModRecipes.registerRecipes();
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		new ModComponents().registerEntityComponentFactories(registry);
	}
}