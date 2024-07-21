package com.example.examplemod.worldgen.biome.custom;

import com.example.examplemod.worldgen.biome.ModBiomes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.joml.Vector3f;

public class CustomBiomeFogAdjuster {

    private final FogProfile fogProfile;
    private final FogProfile targetProfile;
    private final FogProfile activeProfileLerps;

    private int lerpTicksCur = 20 * 15;
    private final int lerpTicksMax = 20 * 15;

    public CustomBiomeFogAdjuster() {
        // Inicializa el perfil de niebla con valores iniciales
        fogProfile = new FogProfile(new Vector3f(0.7F, 0.7F, 0.7F), 0, 20);
        targetProfile = fogProfile;
        activeProfileLerps = new FogProfile(new Vector3f(0F, 0F, 0F), 0, 0);
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

    public void setFogDensity(float start, float end) {
        // Configura los valores de densidad de la niebla
        targetProfile.setFogStart(start);
        targetProfile.setFogEnd(end);
        setupNewLerpRates();
    }

    private void setupNewLerpRates() {
        lerpTicksCur = 0;
        activeProfileLerps.setFogStart(getLerpRate(fogProfile.getFogStart(), targetProfile.getFogStart(), lerpTicksMax));
        activeProfileLerps.setFogEnd(getLerpRate(fogProfile.getFogEnd(), targetProfile.getFogEnd(), lerpTicksMax));
    }

    private float getLerpRate(float curVal, float endVal, float fullLerpTicks) {
        return (endVal - curVal) / fullLerpTicks;
    }

    public void tick() {
        if (lerpTicksCur < lerpTicksMax) {
            fogProfile.setFogStart(fogProfile.getFogStart() + activeProfileLerps.getFogStart());
            fogProfile.setFogEnd(fogProfile.getFogEnd() + activeProfileLerps.getFogEnd());
            lerpTicksCur++;
        }
    }
}
