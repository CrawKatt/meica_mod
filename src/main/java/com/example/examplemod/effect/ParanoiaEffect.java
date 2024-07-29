package com.example.examplemod.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class ParanoiaEffect extends MobEffect {
    private static final Random RANDOM = new Random();
    private int soundTimer;

    protected ParanoiaEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            if(soundTimer > 0) {
                soundTimer--;
            } else if (RANDOM.nextInt(100) < 5) { // 5% de probabilidad de reproducir un sonido en cada tick
                switch (RANDOM.nextInt(6)) {
                    case 0 -> serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ZOMBIE_AMBIENT, entity.getSoundSource(), 1.0F, 1.0F);
                    case 1 -> serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SKELETON_AMBIENT, entity.getSoundSource(), 1.0F, 1.0F);
                    case 2 -> serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CREEPER_PRIMED, entity.getSoundSource(), 1.0F, 1.0F);
                    case 3 -> serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GHAST_WARN, entity.getSoundSource(), 1.0F, 1.0F);
                    case 4 -> serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDERMAN_AMBIENT, entity.getSoundSource(), 1.0F, 1.0F);
                    case 5 -> serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GHAST_HURT, entity.getSoundSource(), 1.0F, 1.0F);
                }
                soundTimer = 200;
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
