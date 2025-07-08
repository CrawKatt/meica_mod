package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.worldgen.biome.ModBiomes;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class FogHandler implements FogModifyCallback {
    private static final float TRANSITION_SPEED = 0.02f;
    private static final float FOG_CUTOFF_THRESHOLD = 0.001f;
    private static final float TARGET_FOG_START = 0.0f;
    private static final float TARGET_FOG_END = 50.0f;
    private static final int BIOME_SAMPLE_RADIUS = 10;
    private static float smoothTransitionProgress = 0.0f;
    private static final RegistryKey<Biome> MEICA_FOREST_KEY = ModBiomes.MEICA_FOREST;

    @Override
    public void modifyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null ||
            client.player.hasStatusEffect(StatusEffects.BLINDNESS) ||
            client.player.hasStatusEffect(StatusEffects.DARKNESS)) {
            return;
        }

        float targetInfluence = getBiomeInfluence(client.player.getBlockPos(), client.world);

        smoothTransitionProgress = MathHelper.lerp(TRANSITION_SPEED * tickDelta, smoothTransitionProgress, targetInfluence);

        if (smoothTransitionProgress > FOG_CUTOFF_THRESHOLD) {
            float easedProgress = smoothStep(smoothTransitionProgress);

            float normalFogStart = RenderSystem.getShaderFogStart();
            float normalFogEnd = RenderSystem.getShaderFogEnd();

            RenderSystem.setShaderFogStart(MathHelper.lerp(easedProgress, normalFogStart, TARGET_FOG_START));
            RenderSystem.setShaderFogEnd(MathHelper.lerp(easedProgress, normalFogEnd, TARGET_FOG_END));
        }
    }

    private float getBiomeInfluence(BlockPos playerPos, World world) {
        int count = 0;
        int targetCount = 0;

        for (int x = -BIOME_SAMPLE_RADIUS; x <= BIOME_SAMPLE_RADIUS; x++) {
            for (int z = -BIOME_SAMPLE_RADIUS; z <= BIOME_SAMPLE_RADIUS; z++) {
                RegistryEntry<Biome> biome = world.getBiome(playerPos.add(x, 0, z));
                if (biome.matchesKey(MEICA_FOREST_KEY)) {
                    targetCount++;
                }
                count++;
            }
        }
        return (float)targetCount / (float)count;
    }

    private static float smoothStep(float x) {
        return x * x * x * (x * (x * 6 - 15) + 10);
    }
}
