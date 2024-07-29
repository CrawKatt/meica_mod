package com.example.examplemod.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;

public class BrotenitaInfectionEffect extends MobEffect {
    private static final int ZOMBIE_SPAWN_INTERVAL = 1200;
    private static int zombieSpawnTimer = 0;

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
        } else if (duration >= 48000) { // 40 minutos
            spawnZombiesAround(entity);
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

    private void spawnZombiesAround(LivingEntity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            if (zombieSpawnTimer <= 0 && serverLevel.random.nextDouble() < 0.25) {
                BlockPos pos = entity.blockPosition();
                for (int index = 0; index < 3; index++) {
                    Zombie zombie = new Zombie(serverLevel);
                    zombie.moveTo(
                            pos.getX() + serverLevel.random.nextInt(10) - 5,
                            pos.getY(),
                            pos.getZ() + serverLevel.random.nextInt(10) - 5
                    );
                    serverLevel.addFreshEntity(zombie);
                }
                zombieSpawnTimer = ZOMBIE_SPAWN_INTERVAL;
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
