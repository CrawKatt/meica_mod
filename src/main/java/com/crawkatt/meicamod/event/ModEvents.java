package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.effect.ModEffects;
import com.crawkatt.meicamod.capabilities.PlayerInfection;
import com.crawkatt.meicamod.capabilities.PlayerInfectionProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
            if (!event.getObject().getCapability(PlayerInfectionProvider.INFECTION).isPresent()) {
                event.addCapability(new ResourceLocation(MeicaMod.MODID, "properties"), new PlayerInfectionProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player player = event.getEntity();
        original.getCapability(PlayerInfectionProvider.INFECTION).ifPresent(oldStore -> {
            player.getCapability(PlayerInfectionProvider.INFECTION).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);
                int remainingDuration = 72000 - oldStore.getInfection();
                if (remainingDuration > 0) {
                    player.addEffect(new MobEffectInstance(ModEffects.BROTIFICATION.get(), remainingDuration, 0));
                }
            });
        });
    }
}
