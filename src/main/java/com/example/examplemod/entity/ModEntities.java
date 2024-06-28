package com.example.examplemod.entity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.brotecito.BrotecitoEntity;
import com.example.examplemod.entity.custom.meica.MeicaEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleMod.MODID);

    // Define la HitBox de los Brotecitos
    public static final RegistryObject<EntityType<BrotecitoEntity>> BROTECITO =
            ENTITY_TYPES.register("brotecito", () -> EntityType.Builder.of(BrotecitoEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 0.6f).build("brotecito"));

    // Define la HitBox de Meica
    public static final RegistryObject<EntityType<MeicaEntity>> MEICA =
            ENTITY_TYPES.register("meica", () -> EntityType.Builder.of(MeicaEntity::new, MobCategory.MONSTER)
                    .sized(1.0f, 2.0f).build("meica"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
