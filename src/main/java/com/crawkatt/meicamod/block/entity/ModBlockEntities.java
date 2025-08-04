package com.crawkatt.meicamod.block.entity;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<MeicaStatueBlockEntity> MEICA_STATUE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MeicaMod.MOD_ID, "meica_statue"),
                    FabricBlockEntityTypeBuilder.create(MeicaStatueBlockEntity::new, ModBlocks.MEICA_STATUE).build(null));

    public static void registerBlockEntities() {
        MeicaMod.LOGGER.info("Registering Block Entities for: {}", MeicaMod.MOD_ID);
    }
}
