package com.example.examplemod.event;

import com.example.examplemod.effect.ModEffects;
import com.example.examplemod.infection.PlayerInfectionProvider;
import com.example.examplemod.worldgen.biome.ModBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber
public class BiomeEvents {

    @SubscribeEvent
    public static void onTickPlayer(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();

        if (level.isClientSide) {
            return;
        }

        ResourceKey<Biome> playerBiomeKey = level.getBiome(player.blockPosition()).unwrapKey().orElse(null);
        if (playerBiomeKey == null) {
            return;
        }

        if (!playerBiomeKey.equals(ModBiomes.MEICA_FOREST)) {
            player.getCapability(PlayerInfectionProvider.TIME_IN_BIOME).ifPresent(capability -> {
                capability.setInfection(0);
                if (player.hasEffect(ModEffects.BROTIFICATION.get())) {
                    player.removeEffect(ModEffects.BROTIFICATION.get());
                }
            });

            return;
        }

        player.getCapability(PlayerInfectionProvider.TIME_IN_BIOME).ifPresent(capability -> {
            capability.addInfection(1);
            int ticksInBiome = capability.getInfection();

            if (player.hasEffect(ModEffects.BROTIFICATION.get())) {
                Optional.ofNullable(player.getEffect(ModEffects.BROTIFICATION.get()))
                        .ifPresent(effectInstance -> effectInstance.update(new MobEffectInstance(ModEffects.BROTIFICATION.get(), ticksInBiome, 0)));
            }

            player.addEffect(new MobEffectInstance(ModEffects.BROTIFICATION.get(), ticksInBiome, 0));
        });
    }
}