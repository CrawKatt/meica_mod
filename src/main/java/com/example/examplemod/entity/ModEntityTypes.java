package com.example.examplemod.entity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.BrotecitoEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, ExampleMod.MODID);

    public static final RegistryObject<EntityType<BrotecitoEntity>> BROTECITO =
            ENTITY_TYPES.register("brotecito",
            () -> EntityType.Builder.of(BrotecitoEntity::new, MobCategory.CREATURE)
                    .sized(0.8f, 0.6f) // Tamaño de la Hitbox del mob
                    .build(new ResourceLocation(ExampleMod.MODID, "brotecito").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
