package com.crawkatt.meicamod.effect;

import com.crawkatt.meicamod.capabilities.PlayerInfection;
import com.crawkatt.meicamod.capabilities.PlayerInfectionProvider;
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
        if (!entity.level().isClientSide && entity instanceof Player player) {
            PlayerInfection infection =  player.getCapability(PlayerInfectionProvider.INFECTION).orElse(null);
            addEffects(entity, infection.getInfection());
        }
        super.applyEffectTick(entity, amplifier);
    }

    private void addEffects(LivingEntity entity, int infectionLevel) {
        if (infectionLevel >= 72000) { // 60 minutos
            entity.kill(); // Muerte
        } else if (infectionLevel >= 60000) { // 50 minutos
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, infectionLevel, 0, true, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, infectionLevel, 0, true, false, false));
            spawnPlayerClonesAround(entity);
        } else if (infectionLevel >= 48000) { // 40 minutos
            spawnPlayerClonesAround(entity);
        } else if (infectionLevel >= 36000) { // 30 minutos
            entity.addEffect(new MobEffectInstance(ModEffects.PARANOIA.get(), infectionLevel, 0, true, false, false));
        } else if (infectionLevel >= 24000) { // 20 minutos
            entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, infectionLevel, 0, true, false, false));
        } else if (infectionLevel >= 12000) { // 10 minutos
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, infectionLevel, 0, true, false, false));
        }
    }

    private void spawnPlayerClonesAround(LivingEntity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            if (cloneSpawnTimer <= 0 && serverLevel.random.nextDouble() < 0.25) {
                BlockPos pos = entity.blockPosition();
                PlayerCloneEntity clone = new PlayerCloneEntity(ModEntities.PLAYER_CLONE.get(), serverLevel);
                clone.moveTo(
                        pos.getX() + serverLevel.random.nextInt(10) - 5,
                        pos.getY(),
                        pos.getZ() + serverLevel.random.nextInt(10) - 5
                );

                if (entity instanceof Player player) {
                    clone.copyInventory(player);
                    clone.copyArmor(player);
                    clone.setTarget(player);
                }

                serverLevel.addFreshEntity(clone);
                cloneSpawnTimer = CLONE_SPAWN_INTERVAL;
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
