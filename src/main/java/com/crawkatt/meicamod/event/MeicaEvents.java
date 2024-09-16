package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.meica.MeicaEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MeicaMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MeicaEvents {

    // Evento para reproducir un sonido cuando Meica mata a una entidad
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof MeicaEntity meica && event.getEntity() instanceof Player) {
            meica.playKillSound();
        }
    }
}