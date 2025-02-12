package com.crawkatt.meicamod.particle;

import com.crawkatt.meicamod.MeicaMod;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {
    public static final DefaultParticleType KAPPA_PRIDE_PARTICLES =
            registerParticle("kappa_pride_particles", FabricParticleTypes.simple());

    private static DefaultParticleType registerParticle(String name, DefaultParticleType particleType) {
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(MeicaMod.MOD_ID, name), particleType);
    }

    public static void registerParticles() {
        MeicaMod.LOGGER.info("Registering Particles for " + MeicaMod.MOD_ID);
    }
}
