package com.crawkatt.meicamod.effect;

import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.player_clone.PlayerCloneEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class BrotenitaInfectionEffect extends MobEffect {
    private static final int CLONE_SPAWN_INTERVAL = 7200;
    private static int cloneSpawnTimer = 0;

    public BrotenitaInfectionEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            addEffects(entity, this.getDuration(entity));
        }
        super.applyEffectTick(entity, amplifier);
    }

    private void addEffects(LivingEntity entity, int duration) {
        if (duration >= 72000) { // 60 minutos
            entity.kill(); // Muerte
        } else if (duration >= 60000) { // 50 minutos
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 32767, 0));
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 32767, 0));
            spawnPlayerClonesAround(entity);
        } else if (duration >= 48000) { // 40 minutos
            spawnPlayerClonesAround(entity);
        } else if (duration >= 36000) { // 30 minutos
            entity.addEffect(new MobEffectInstance(ModEffects.PARANOIA.get(), 32767, 0));
        } else if (duration >= 24000) { // 20 minutos
            entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 32767, 0));
        } else if (duration >= 12000) { // 10 minutos
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 32767, 0));
        }
    }

    private int getDuration(LivingEntity entity) {
        for (MobEffectInstance effectInstance : entity.getActiveEffects()) {
            if (effectInstance.getEffect() == this) {
                return effectInstance.getDuration();
            }
        }
        return 0;
    }

    private void spawnPlayerClonesAround(LivingEntity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            if (cloneSpawnTimer <= 0 && serverLevel.random.nextDouble() < 0.25) {
                BlockPos pos = entity.blockPosition();
                for (int index = 0; index < 3; index++) {
                    PlayerCloneEntity clone = new PlayerCloneEntity(ModEntities.PLAYER_CLONE.get(), serverLevel);
                    clone.moveTo(
                            pos.getX() + serverLevel.random.nextInt(10) - 5,
                            pos.getY(),
                            pos.getZ() + serverLevel.random.nextInt(10) - 5
                    );
                    serverLevel.addFreshEntity(clone);
                    if (entity instanceof Player player) {
                        clone.setTarget(player); // Establecer al jugador como objetivo
                    }
                }
                cloneSpawnTimer = CLONE_SPAWN_INTERVAL;
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
