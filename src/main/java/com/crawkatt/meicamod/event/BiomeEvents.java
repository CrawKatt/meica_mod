package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.effect.ModEffects;
import com.crawkatt.meicamod.capabilities.PlayerInfectionProvider;
import com.crawkatt.meicamod.worldgen.biome.ModBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BiomeEvents {
    private static final ResourceKey<Biome> TARGET_BIOME = ModBiomes.MEICA_FOREST;

    @SubscribeEvent
    public static void onTickPlayer(TickEvent.PlayerTickEvent event) {
        if (event.side != LogicalSide.SERVER || event.phase != TickEvent.Phase.END) {
            return;
        }

        Player player = event.player;
        Level level = player.level();

        ResourceKey<Biome> currentBiome = level.getBiome(player.blockPosition()).unwrapKey().orElse(null);
        player.getCapability(PlayerInfectionProvider.INFECTION).ifPresent(infection -> {
            if (player.hasEffect(ModEffects.FOREST_BLESSING.get())) {
                infection.setInfection(0);
                player.removeEffect(ModEffects.BROTIFICATION.get());
                return;
            }

            if (TARGET_BIOME.equals(currentBiome)) {
                infection.addInfection(1);
                player.addEffect(new MobEffectInstance(
                        ModEffects.BROTIFICATION.get(),
                        infection.getInfection(),
                        0,
                        true,
                        false,
                        true
                ));
            } else {
                infection.substractInfection(1);
                if (infection.getInfection() <= 0) {
                    player.removeEffect(ModEffects.BROTIFICATION.get());
                }
            }
        });
    }
}