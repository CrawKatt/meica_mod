package com.crawkatt.meicamod.mixin;

import com.crawkatt.meicamod.worldgen.biome.ModBiomes;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public abstract class FogRendererMixin {
    @Unique
    private static float transitionProgress = 0.0f;
    @Unique
    private static final float TRANSITION_SPEED = 0.008f;
    @Unique
    private static final float FOG_CUTOFF_THRESHOLD = 0.001f;

    @Inject(method = "applyFog", at = @At("TAIL"))
    private static void modifyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance,
                                  boolean thickFog, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        if (client.player.hasStatusEffect(StatusEffects.BLINDNESS) || client.player.hasStatusEffect(StatusEffects.DARKNESS)) return;

        RegistryEntry<Biome> biome = client.world.getBiome(client.player.getBlockPos());
        boolean isInTargetBiome = biome.matchesKey(ModBiomes.MEICA_FOREST);

        float transitionDelta = TRANSITION_SPEED * (client.isPaused() ? 0.0f : tickDelta);
        transitionProgress = MathHelper.clamp(
                transitionProgress + (isInTargetBiome ? transitionDelta : -transitionDelta),
                0.0f,
                1.0f
        );

        float normalFogStart = 0.0f;
        float normalFogEnd = getDefaultFogEnd(client, viewDistance);

        float targetFogStart = 0.0f;
        float targetFogEnd = 50.0f;

        float easedProgress = smoothStep(transitionProgress);

        if (transitionProgress > FOG_CUTOFF_THRESHOLD) {
            RenderSystem.setShaderFogStart(MathHelper.lerp(easedProgress, normalFogStart, targetFogStart));
            RenderSystem.setShaderFogEnd(MathHelper.lerp(easedProgress, normalFogEnd, targetFogEnd));
        } else {
            RenderSystem.setShaderFogStart(normalFogStart);
            RenderSystem.setShaderFogEnd(normalFogEnd);
            transitionProgress = 0.0f;
        }
    }

    @Unique
    private static float getDefaultFogEnd(MinecraftClient client, float viewDistance) {
        return client.options.getClampedViewDistance() * viewDistance;
    }

    @Unique
    private static float smoothStep(float x) {
        return x * x * x * (x * (x * 6 - 15) + 10);
    }
}