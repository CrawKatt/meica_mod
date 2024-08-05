package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.brotecito.BrotecitoEntity;
import com.crawkatt.meicamod.entity.custom.brotecito.BrotecitoMamadoEntity;
import com.crawkatt.meicamod.entity.custom.meica.MeicaEntity;
import com.crawkatt.meicamod.entity.custom.player_clone.PlayerCloneEntity;
import com.crawkatt.meicamod.particle.ModParticles;
import com.crawkatt.meicamod.particle.custom.KappaPrideParticles;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MeicaMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BROTECITO.get(), BrotecitoEntity.createAttributes());
        event.put(ModEntities.BROTECITO_MAMADO.get(), BrotecitoMamadoEntity.createAttributes());
        event.put(ModEntities.MEICA.get(), MeicaEntity.createAttributes());
        event.put(ModEntities.PLAYER_CLONE.get(), PlayerCloneEntity.createAttributes());
    }

    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.KAPPA_PRIDE_PARTICLES.get(), KappaPrideParticles.Provider::new);
    }
}
