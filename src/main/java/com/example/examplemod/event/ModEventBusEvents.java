package com.example.examplemod.event;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.ModEntities;
import com.example.examplemod.entity.custom.brotecito.BrotecitoEntity;
import com.example.examplemod.entity.custom.meica.MeicaEntity;
import com.example.examplemod.particle.ModParticles;
import com.example.examplemod.particle.custom.KappaPrideParticles;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BROTECITO.get(), BrotecitoEntity.createAttributes().build());
        event.put(ModEntities.MEICA.get(), MeicaEntity.createMobAttributes().build());
    }

    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.KAPPA_PRIDE_PARTICLES.get(), KappaPrideParticles.Provider::new);
    }
}
