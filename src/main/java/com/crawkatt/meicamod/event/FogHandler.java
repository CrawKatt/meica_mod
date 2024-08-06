package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent.RenderFog;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MeicaMod.MODID, value = Dist.CLIENT)
public class FogHandler {
    private static ResourceKey<Biome> currentBiome = null;
    private static long timeInBiome = 0;
    private static final long TRANSITION_DURATION = 100; // Duración de la transición en ticks (100 ticks = 5 segundos)

    @SubscribeEvent
    public static void onRenderFog(RenderFog event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null) return;

        ResourceKey<Biome> biomeKey = player.level().getBiome(player.blockPosition()).unwrapKey().orElse(null);
        if (biomeKey != null && biomeKey.equals(ResourceKey.create(Registries.BIOME, new ResourceLocation(MeicaMod.MODID, "meica_forest")))) {
            // Si el jugador está en el bioma personalizado
            if (!biomeKey.equals(currentBiome)) {
                currentBiome = biomeKey;
                timeInBiome = 0;
            } else {
                timeInBiome++;
            }

            float transitionFactor = Math.min(1.0f, (float) timeInBiome / TRANSITION_DURATION);
            float nearPlaneDistance = 0.1f + (1.0f - 0.1f) * (1.0f - transitionFactor);
            float farPlaneDistance = 50.0f + (100.0f - 50.0f) * (1.0f - transitionFactor);

            event.setNearPlaneDistance(nearPlaneDistance);
            event.setFarPlaneDistance(farPlaneDistance);
            event.setCanceled(true);

        } else {
            // Si el jugador sale del bioma, reiniciar el tiempo
            if (currentBiome != null) {
                currentBiome = null;
                timeInBiome = 0;
            }
        }
    }
}
