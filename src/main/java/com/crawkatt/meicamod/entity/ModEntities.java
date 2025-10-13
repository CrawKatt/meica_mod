package com.crawkatt.meicamod.entity;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.BrotecitoEntity;
import com.crawkatt.meicamod.entity.custom.BrotecitoMamadoEntity;
import com.crawkatt.meicamod.entity.custom.MeicaEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;

public class ModEntities {
    public static final EntityType<MeicaEntity> MEICA = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MeicaMod.MOD_ID, "meica"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MeicaEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 2.0f)).build());

    public static final EntityType<BrotecitoEntity> BROTECITO = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MeicaMod.MOD_ID, "brotecito"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BrotecitoEntity::new)
                    .dimensions(EntityDimensions.fixed(1f, 1f))
                    .specificSpawnBlocks(Blocks.DIRT, Blocks.GRASS_BLOCK)
                    .build());

    public static final EntityType<BrotecitoMamadoEntity> BROTECITO_MAMADO = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MeicaMod.MOD_ID, "brotecito_mamado"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BrotecitoMamadoEntity::new)
                    .dimensions(EntityDimensions.fixed(1.4F, 2.7F)).build());


    public static void registerModEntities() {
        MeicaMod.LOGGER.info("Registering Mod Entities for " + MeicaMod.MOD_ID);
        SpawnRestriction.register(
                BROTECITO,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                ModEntities::canBrotecitoSpawn
        );
    }

    private static boolean canBrotecitoSpawn(
            EntityType<BrotecitoEntity> type,
            ServerWorldAccess world,
            SpawnReason reason,
            BlockPos pos,
            Random random
    ) {
        BlockState below = world.getBlockState(pos.down());
        return world.getFluidState(pos).isEmpty()
                && below.isOpaqueFullCube(world, pos.down());
    }
}
