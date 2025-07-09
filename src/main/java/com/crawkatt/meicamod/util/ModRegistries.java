package com.crawkatt.meicamod.util;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.command.LocateCustomBiomeCommand;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.BrotecitoEntity;
import com.crawkatt.meicamod.entity.custom.BrotecitoMamadoEntity;
import com.crawkatt.meicamod.entity.custom.MeicaEntity;
import com.crawkatt.meicamod.event.MeicaEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

public class ModRegistries {
    public static void registerModStuffs() {
        registerEvents();
        registerFlammables();
        registerAttributes();
        registerCommands();
    }

    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntities.MEICA, MeicaEntity.createMeicaAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.BROTECITO_MAMADO, BrotecitoMamadoEntity.createBrotecitoMamadoAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.BROTECITO, BrotecitoEntity.createBrotecitoAttributes());
    }

    private static void registerFlammables() {
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.HOLLOW_OAK_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_HOLLOW_OAK_LOG, 5, 5);
    }

    private static void registerEvents() {
        ServerLivingEntityEvents.AFTER_DEATH.register(new MeicaEvents());
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(LocateCustomBiomeCommand::register);
    }
}
