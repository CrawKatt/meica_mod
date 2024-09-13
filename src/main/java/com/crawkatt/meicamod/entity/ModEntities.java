package com.crawkatt.meicamod.entity;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.brotecito.BrotecitoEntity;
import com.crawkatt.meicamod.entity.custom.brotecito.BrotecitoMamadoEntity;
import com.crawkatt.meicamod.entity.custom.meica.MeicaEntity;
import com.crawkatt.meicamod.entity.custom.player_clone.PlayerCloneEntity;
import com.crawkatt.meicamod.entity.projectile.IceArrow;
import com.crawkatt.meicamod.entity.projectile.PoisonArrow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MeicaMod.MODID);

    // Entidades
    // Define la HitBox de los Brotecitos
    public static final RegistryObject<EntityType<BrotecitoEntity>> BROTECITO =
            ENTITY_TYPES.register("brotecito", () -> EntityType.Builder.of(BrotecitoEntity::new, MobCategory.CREATURE)
                    .sized(1f, 1f).build("brotecito"));

    // Define la HitBox de Meica
    public static final RegistryObject<EntityType<MeicaEntity>> MEICA =
            ENTITY_TYPES.register("meica", () -> EntityType.Builder.of(MeicaEntity::new, MobCategory.MONSTER)
                    .sized(1.0f, 2.0f).build("meica"));

    // Define la HitBox del Brotecito Mamado
    public static final RegistryObject<EntityType<BrotecitoMamadoEntity>> BROTECITO_MAMADO =
            ENTITY_TYPES.register("brotecito_mamado", () -> EntityType.Builder.of(BrotecitoMamadoEntity::new, MobCategory.CREATURE)
                    .sized(1.4F, 2.7F).build("brotecito_mamado"));

    public static final RegistryObject<EntityType<PlayerCloneEntity>> PLAYER_CLONE =
            ENTITY_TYPES.register("player_clone", () -> EntityType.Builder.of(PlayerCloneEntity::new, MobCategory.MONSTER)
                    .sized(1.0F, 2.0F).build("player_clone"));

    // Flechas
    public static final RegistryObject<EntityType<PoisonArrow>> POISON_ARROW =
            ENTITY_TYPES.register("poison_arrow", () -> EntityType.Builder.<PoisonArrow>of(PoisonArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("poison_arrow"));

    public static final RegistryObject<EntityType<IceArrow>> ICE_ARROW =
            ENTITY_TYPES.register("ice_arrow", () -> EntityType.Builder.<IceArrow>of(IceArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("ice_arrow"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
