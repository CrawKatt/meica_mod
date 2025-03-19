package com.crawkatt.meicamod.util;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.command.SpawnClonesCommand;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.BrotecitoEntity;
import com.crawkatt.meicamod.entity.custom.BrotecitoMamadoEntity;
import com.crawkatt.meicamod.entity.custom.MeicaEntity;
import com.crawkatt.meicamod.entity.custom.PlayerCloneEntity;
import com.crawkatt.meicamod.event.*;
import com.crawkatt.meicamod.item.ModItems;
import com.crawkatt.meicamod.mixin.BrewingRecipeRegistryMixin;
import com.crawkatt.meicamod.potion.ModPotions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ModRegistries {
    public static void registerModStuffs() {
        createPortal();
        registerEvents();
        registerAttributes();
        registerCommands();
        registerPotionRecipes();
    }

    private static void createPortal() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(ModBlocks.BROTENITA_BLOCK)
                .lightWithItem(ModItems.BROTENITA_STAFF)
                .destDimID(new Identifier(MeicaMod.MOD_ID, "meicadim"))
                .tintColor(0x00740b)
                .onlyLightInOverworld()
                .registerIgniteEvent(((player, world, portalPos, framePos, portalIgnitionSource) -> world.playSound(
                        null,
                        framePos,
                        SoundEvents.BLOCK_END_PORTAL_SPAWN,
                        SoundCategory.BLOCKS,
                        1.0F,
                        1.0F
                )))
                .registerPortal();
    }

    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntities.MEICA, MeicaEntity.createMeicaAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.BROTECITO_MAMADO, BrotecitoMamadoEntity.createBrotecitoMamadoAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.BROTECITO, BrotecitoEntity.createBrotecitoAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.PLAYER_CLONE, PlayerCloneEntity.createPlayerCloneAttributes());
    }

    private static void registerEvents() {
        ServerLivingEntityEvents.AFTER_DEATH.register(new MeicaEvents());
        ServerLivingEntityEvents.AFTER_DEATH.register(new BossDeathHandler());
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(new DimensionEvents());
        ServerTickEvents.END_WORLD_TICK.register(new BiomeEvents());
    }

    private static void registerPotionRecipes() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.BROTENITA_MEAL, ModPotions.BLESSING_FOREST_POTION);
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(SpawnClonesCommand::register);
    }
}
