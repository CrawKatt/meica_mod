package com.crawkatt.meicamod.block.entity;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import team.reborn.energy.api.EnergyStorage;

public class ModBlockEntities {
    public static final BlockEntityType<BrotenitaMelterBlockEntity> BROTENITA_MELTER_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MeicaMod.MOD_ID, "brotenita_melter"),
                    FabricBlockEntityTypeBuilder.create(BrotenitaMelterBlockEntity::new,
                            ModBlocks.BROTENITA_MELTER).build(null));

    public static void registerBlockEntities() {
        MeicaMod.LOGGER.info("Registering Block Entities for " + MeicaMod.MOD_ID);

        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.ironStorage, BROTENITA_MELTER_BE);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidStorage, BROTENITA_MELTER_BE);
    }
}
