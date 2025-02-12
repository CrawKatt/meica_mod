package com.crawkatt.meicamod.capabilities;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

public class PlayerInfectionProvider {
    private static final String BIOME_TIME_KEY = "MeicaTime";

    public static void updateBiomeTime(ServerPlayerEntity player, boolean isInBiome) {
        //NbtCompound nbt = player.getPersistentData();
        //int currentTime = nbt.getInt(BIOME_TIME_KEY);

        // Actualizar tiempo (máximo 600 ticks = 30 segundos)
        //currentTime = MathHelper.clamp(currentTime + (isInBiome ? 1 : -3), 0, 600);
        //nbt.putInt(BIOME_TIME_KEY, currentTime);

        //applyEffects(player, currentTime);
    }

    private static void applyEffects(ServerPlayerEntity player, int time) {
        player.removeStatusEffect(StatusEffects.WEAKNESS);
        player.removeStatusEffect(StatusEffects.SLOWNESS);
        player.removeStatusEffect(StatusEffects.BLINDNESS);

        // Escalado de efectos según tiempo acumulado
        if(time > 400) { // 20 segundos
            applyEffect(player, StatusEffects.BLINDNESS, 0);
        }
        if(time > 200) { // 10 segundos
            applyEffect(player, StatusEffects.SLOWNESS, 1);
        }
        if(time > 100) { // 5 segundos
            applyEffect(player, StatusEffects.WEAKNESS, 2);
        }
    }

    private static void applyEffect(ServerPlayerEntity player, StatusEffect effect, int amplifier) {
        player.addStatusEffect(new StatusEffectInstance(
                effect,
                80, // 4 segundos de duración
                amplifier,
                true, // partículas visibles
                true, // mostrar icono
                true // puede desactivarse con leche
        ));
    }
}
