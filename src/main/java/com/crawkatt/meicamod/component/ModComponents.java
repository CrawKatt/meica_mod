package com.crawkatt.meicamod.component;

import com.crawkatt.meicamod.MeicaMod;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class ModComponents implements EntityComponentInitializer {
    public static final ComponentKey<PlayerInfectionComponent> INFECTION =
            ComponentRegistry.getOrCreate(new Identifier(MeicaMod.MOD_ID, "infection"), PlayerInfectionComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(
                INFECTION,
                player -> new PlayerInfectionComponent(),
                RespawnCopyStrategy.ALWAYS_COPY
        );
    }
}
