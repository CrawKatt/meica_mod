package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.component.ModComponents;
import com.crawkatt.meicamod.component.PlayerInfectionComponent;
import com.crawkatt.meicamod.effect.ModEffects;
import com.crawkatt.meicamod.worldgen.biome.ModBiomes;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.biome.Biome;

public class BiomeEvents implements ServerTickEvents.EndWorldTick {
    private static final RegistryKey<Biome> TARGET_BIOME = ModBiomes.MEICA_FOREST;

    @Override
    public void onEndTick(ServerWorld world) {
        world.getPlayers().forEach(player -> {
            RegistryKey<Biome> currentBiome = world.getBiome(player.getBlockPos()).getKey().orElse(null);
            PlayerInfectionComponent infection = ModComponents.INFECTION.get(player);

            if (player.hasStatusEffect(ModEffects.FOREST_BLESSING)) {
                infection.setInfection(0);
                player.removeStatusEffect(ModEffects.BROTIFICATION);
                return;
            }

            if (TARGET_BIOME.equals(currentBiome)) {
                infection.addInfection(1, player);
                player.addStatusEffect(new StatusEffectInstance(
                        ModEffects.BROTIFICATION,
                        infection.getInfection(),
                        0,
                        true,
                        false,
                        true
                ));
            } else {
                infection.subtractInfection(1, player);
                if (infection.getInfection() <= 0) {
                    player.removeStatusEffect(ModEffects.BROTIFICATION);
                }
            }
        });
    }
}
