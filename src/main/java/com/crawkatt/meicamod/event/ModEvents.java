package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.effect.ModEffects;
import com.crawkatt.meicamod.infection.PlayerInfection;
import com.crawkatt.meicamod.infection.PlayerInfectionProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MeicaMod.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerInfection.class);
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Player> event) {
        if (event.getObject() != null) {
            event.addCapability(new ResourceLocation(MeicaMod.MODID, "time_in_biome"), new PlayerInfectionProvider());
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerInfectionProvider.TIME_IN_BIOME).isPresent()) {
                event.addCapability(new ResourceLocation(MeicaMod.MODID, "properties"), new PlayerInfectionProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player player = event.getEntity();
        original.getCapability(PlayerInfectionProvider.TIME_IN_BIOME).ifPresent(oldStore -> {
            player.getCapability(PlayerInfectionProvider.TIME_IN_BIOME).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);
                int remainingDuration = 72000 - oldStore.getInfection();
                if (remainingDuration > 0) {
                    player.addEffect(new MobEffectInstance(ModEffects.BROTIFICATION.get(), remainingDuration, 0));
                }
            });
        });
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            event.player.getCapability(PlayerInfectionProvider.TIME_IN_BIOME).ifPresent(infection -> {
                if (infection.getInfection() > 0 && event.player.getRandom().nextFloat() < 0.005f) {
                    infection.subInfection(1);
                }
            });
        }
    }
}
