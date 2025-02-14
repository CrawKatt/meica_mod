package com.crawkatt.meicamod.effect;

import com.crawkatt.meicamod.component.PlayerInfectionState;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.PlayerCloneEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class BrotenitaInfectionEffect extends StatusEffect {
    private static final int CLONE_SPAWN_INTERVAL = 7200;
    private static int cloneSpawnTimer = 0;

    protected BrotenitaInfectionEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.getWorld().isClient && entity instanceof ServerPlayerEntity player) {
            int infectionLevel = PlayerInfectionState.get(player).getInfection().getInfection();
            addEffects(entity, infectionLevel);
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    private void addEffects(LivingEntity entity, int infectionLevel) {
        if (infectionLevel >= 72000) { // 60 minutos
            entity.kill(); // Muerte
        } else if (infectionLevel >= 60000) { // 50 minutos
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 32767, 0, true, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 32767, 0, true, false, false));
            spawnPlayerClonesAround(entity);
        } else if (infectionLevel >= 48000) { // 40 minutos
            spawnPlayerClonesAround(entity);
        } else if (infectionLevel >= 36000) { // 30 minutos
            entity.addStatusEffect(new StatusEffectInstance(ModEffects.PARANOIA, 32767, 0, true, false, false));
        } else if (infectionLevel >= 24000) { // 20 minutos
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 32767, 0, true, false, false));
        } else if (infectionLevel >= 12000) { // 10 minutos
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 32767, 0, true, false, false));
        }
    }

    /*
    private int getDuration(LivingEntity entity) {
        for (StatusEffectInstance effectInstance : entity.getStatusEffects()) {
            if (effectInstance.getEffectType() == this) {
                return effectInstance.getDuration();
            }
        }
        return 0;
    }
    */

    private void spawnPlayerClonesAround(LivingEntity entity) {
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            if (cloneSpawnTimer <= 0 && serverWorld.random.nextDouble() < 0.25) {
                BlockPos pos = entity.getBlockPos();
                PlayerCloneEntity clone = new PlayerCloneEntity(ModEntities.PLAYER_CLONE, serverWorld);
                clone.refreshPositionAfterTeleport(
                        pos.getX() + serverWorld.random.nextInt(10) - 5,
                        pos.getY(),
                        pos.getZ() + serverWorld.random.nextInt(10) - 5
                );

                if (entity instanceof PlayerEntity player) {
                    clone.copyInventory(player);
                    clone.copyArmor(player);
                    clone.setTarget(player);
                }

                serverWorld.spawnEntity(clone);
                cloneSpawnTimer = CLONE_SPAWN_INTERVAL;
            }
        }
    }
}
