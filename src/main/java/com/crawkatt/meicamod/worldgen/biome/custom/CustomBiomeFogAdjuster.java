package com.crawkatt.meicamod.worldgen.biome.custom;

import com.crawkatt.meicamod.worldgen.biome.ModBiomes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.joml.Vector3f;

public class CustomBiomeFogAdjuster {

    private final FogProfile fogProfile;
    private float fogDensity = 1500.0F;
    private boolean wasInBiome = false;

    private static final float MIN_FOG_DENSITY = 50.0F; // Límite mínimo para la densidad de la niebla
    private static final float MAX_FOG_DENSITY = 1500.0F; // Límite máximo para la densidad de la niebla

    public CustomBiomeFogAdjuster() {
        // Inicializa el perfil de niebla con valores iniciales
        fogProfile = new FogProfile(new Vector3f(0.7F, 0.7F, 0.7F), 0, MAX_FOG_DENSITY);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tick();
        }
    }

    @SubscribeEvent
    public void onFogColors(ViewportEvent.ComputeFogColor event) {
        // Obtiene la instancia del mundo y la posición del jugador
        Level level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;

        if (level != null && player != null) {
            ResourceKey<Biome> playerBiomeKey = level.getBiome(player.blockPosition()).unwrapKey().orElse(null);

            if (playerBiomeKey != null && playerBiomeKey.equals(ModBiomes.MEICA_FOREST)) {
                fogProfile.getRgb().set(event.getRed(), event.getGreen(), event.getBlue());
                float brightness = Mth.clamp(Mth.cos(level.getTimeOfDay(1F) * ((float)Math.PI * 2F)) * 2.0F + 0.5F, 0.0F, 1.0F);
                event.setRed(fogProfile.getRgb().x() * brightness);
                event.setGreen(fogProfile.getRgb().y() * brightness);
                event.setBlue(fogProfile.getRgb().z() * brightness);
            }
        }
    }

    @SubscribeEvent
    public void onFogRender(ViewportEvent.RenderFog event) {
        Level level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;
        if (level != null && player != null) {
            ResourceKey<Biome> playerBiomeKey = level.getBiome(player.blockPosition()).unwrapKey().orElse(null);

            if (playerBiomeKey != null && playerBiomeKey.equals(ModBiomes.MEICA_FOREST)) {
                event.setNearPlaneDistance(fogProfile.getFogStart());
                event.setFarPlaneDistance(fogProfile.getFogEnd());
                event.setCanceled(true);
            }
        }
    }

    public void setFogDensity(float end) {
        fogProfile.setFogEnd(end);
    }

    public void tick() {
        Level level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;
        if (level != null && player != null) {
            ResourceKey<Biome> playerBiomeKey = level.getBiome(player.blockPosition()).unwrapKey().orElse(null);
            boolean isInBiome = playerBiomeKey != null && playerBiomeKey.equals(ModBiomes.MEICA_FOREST);

            if (isInBiome) {
                if (!wasInBiome) {
                    fogDensity = MAX_FOG_DENSITY;
                    wasInBiome = true;
                }
                fogDensity = Math.max(fogDensity - 50.0F, MIN_FOG_DENSITY);
            } else {
                if (wasInBiome) {
                    fogDensity = MAX_FOG_DENSITY;
                    wasInBiome = false;
                }
                fogDensity = Math.min(fogDensity + 50.0F, MAX_FOG_DENSITY);
            }

            setFogDensity(fogDensity);
        }
    }
}
