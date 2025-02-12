package com.crawkatt.meicamod.effect;

import com.crawkatt.meicamod.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;

import java.util.Random;

public class ParanoiaEffect extends StatusEffect {
    public static final Random RANDOM = new Random();
    private int soundTimer;

    protected ParanoiaEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            if(soundTimer > 0) {
                soundTimer--;
            } else if (RANDOM.nextInt(100) < 5) { // 5% de probabilidad de reproducir un sonido en cada tick
                switch (RANDOM.nextInt(6)) {
                    case 0 -> serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.MEICA_LAUGHT, entity.getSoundCategory(), 1.0F, 1.0F);
                    case 1 -> serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.MEICA_HALLO, entity.getSoundCategory(), 1.0F, 1.0F);
                    case 2 -> serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_CREEPER_PRIMED, entity.getSoundCategory(), 1.0F, 1.0F);
                    case 3 -> serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_GHAST_WARN, entity.getSoundCategory(), 1.0F, 1.0F);
                    case 4 -> serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_AMBIENT, entity.getSoundCategory(), 1.0F, 1.0F);
                    case 5 -> serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_GHAST_HURT, entity.getSoundCategory(), 1.0F, 1.0F);
                }
                soundTimer = 200;
            }
        }
    }
}
