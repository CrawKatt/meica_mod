package com.crawkatt.meicamod.entity;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<MeicaEntity> MEICA = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MeicaMod.MOD_ID, "meica"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MeicaEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 2.0f)).build());

    public static final EntityType<ZeroFiveEntity> ZERO_FIVE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MeicaMod.MOD_ID, "05"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ZeroFiveEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 2.0f)).build());

    public static final EntityType<BrotecitoEntity> BROTECITO = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MeicaMod.MOD_ID, "brotecito"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BrotecitoEntity::new)
                    .dimensions(EntityDimensions.fixed(1f, 1f)).build());

    public static final EntityType<BrotecitoMamadoEntity> BROTECITO_MAMADO = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MeicaMod.MOD_ID, "brotecito_mamado"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BrotecitoMamadoEntity::new)
                    .dimensions(EntityDimensions.fixed(1.4F, 2.7F)).build());

    public static final EntityType<PlayerCloneEntity> PLAYER_CLONE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MeicaMod.MOD_ID, "player_clone"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PlayerCloneEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 2.0f)).build());

    public static final EntityType<MagicCircleEntity> MAGIC_CIRCLE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MeicaMod.MOD_ID, "magic_circle"),
            EntityType.Builder.create(MagicCircleEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.1f, 0.1f)
                    .maxTrackingRange(1)
                    .build("magic_circle")
    );

    public static void registerModEntities() {
        MeicaMod.LOGGER.info("Registering Mod Entities for " + MeicaMod.MOD_ID);
    }
}
