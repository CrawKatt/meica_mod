package com.crawkatt.meicamod.block.entity;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MeicaMod.MODID);

    public static final RegistryObject<BlockEntityType<BrotenitaMelterBlockEntity>> BROTENITA_MELTER_BE =
            BLOCK_ENTITIES.register("brotenita_melter", () ->
                    BlockEntityType.Builder.of(BrotenitaMelterBlockEntity::new, ModBlocks.BROTENITA_MELTER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
