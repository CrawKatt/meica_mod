package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.component.PlayerInfectionState;
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
            // Obtener el bioma actual del jugador
            RegistryKey<Biome> currentBiome = world.getBiome(player.getBlockPos()).getKey().orElse(null);
            PlayerInfectionState infectionState = PlayerInfectionState.get(player);

            if (TARGET_BIOME.equals(currentBiome)) {
                infectionState.getInfection().addInfection(1);
                infectionState.markDirty();

                // Aplicar efecto si la infección supera cierto umbral
                if (infectionState.getInfection().getInfection() > 200 && !player.hasStatusEffect(ModEffects.BROTIFICATION)) {
                    player.addStatusEffect(new StatusEffectInstance(
                            ModEffects.BROTIFICATION,
                            72000,
                            0,
                            true,
                            false,
                            true
                    ));
                }
            } else {
                infectionState.getInfection().subInfection(1);
                if (infectionState.getInfection().getInfection() <= 0) {
                    player.removeStatusEffect(ModEffects.BROTIFICATION);
                }
            }
        });
    }
}
