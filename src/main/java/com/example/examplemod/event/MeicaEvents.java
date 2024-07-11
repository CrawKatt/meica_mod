package com.example.examplemod.event;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.meica.MeicaEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MeicaEvents {

    // Evento para reproducir un sonido cuando Meica mata a una entidad
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof MeicaEntity meica) {
            // Llama al método para reproducir el sonido
            meica.playKillSound();
        }
    }
}