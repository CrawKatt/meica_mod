package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.worldgen.biome.ModBiomes;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent.RenderFog;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MeicaMod.MODID, value = Dist.CLIENT)
public class FogHandler {
    private static float transitionProgress = 0.0f;
    private static final float TRANSITION_SPEED = 0.008f;
    private static final float FOG_CUTOFF_THRESHOLD = 0.001f;

    @SubscribeEvent
    public static void onRenderFog(RenderFog event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) return;
        if (minecraft.player.hasEffect(MobEffects.BLINDNESS) || minecraft.player.hasEffect(MobEffects.DARKNESS)) return;

        Holder<Biome> biome = minecraft.level.getBiome(minecraft.player.getOnPos());
        boolean isInTargetBiome = biome.is(ModBiomes.MEICA_FOREST);

        float transitionDelta = (float) (TRANSITION_SPEED * (minecraft.isPaused() ? 0.0F : event.getPartialTick()));
        transitionProgress = Mth.clamp(
                transitionProgress + (isInTargetBiome ? transitionDelta : -transitionDelta),
                0.0f,
                1.0f
        );

        float normalFogStart = RenderSystem.getShaderFogStart();
        float normalFogEnd = RenderSystem.getShaderFogEnd();

        float targetFogStart = 0.0f;
        float targetFogEnd = 50.0f;

        float easedProgress = smoothStep(transitionProgress);

        if (transitionProgress > FOG_CUTOFF_THRESHOLD) {
            RenderSystem.setShaderFogStart(Mth.lerp(easedProgress, normalFogStart, targetFogStart));
            RenderSystem.setShaderFogEnd(Mth.lerp(easedProgress, normalFogEnd, targetFogEnd));
        } else {
            RenderSystem.setShaderFogStart(normalFogStart);
            RenderSystem.setShaderFogEnd(normalFogEnd);
            transitionProgress = 0.0f;
        }
    }

    private static float smoothStep(float x) {
        return x * x * x * (x * (x * 6 - 15) + 10);
    }
}
