package com.crawkatt.meicamod.effect;

import com.crawkatt.meicamod.component.ModComponents;
import com.crawkatt.meicamod.component.PlayerInfectionComponent;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.PlayerCloneEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
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
        if (!entity.getWorld().isClient) {
            PlayerInfectionComponent infection = ModComponents.INFECTION.get(entity);
            addEffects(entity, infection.getInfection());
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    private void addEffects(LivingEntity entity, int infectionLevel) {
        if (infectionLevel >= 72000) {
            entity.kill();
            ModComponents.INFECTION.get(entity).setInfection(0);
        } else if (infectionLevel >= 60000) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, infectionLevel, 0, true, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, infectionLevel, 0, true, false, false));
            spawnPlayerClonesAround(entity);
        } else if (infectionLevel >= 48000) {
            spawnPlayerClonesAround(entity);
        } else if (infectionLevel >= 36000) {
            entity.addStatusEffect(new StatusEffectInstance(ModEffects.PARANOIA, infectionLevel, 0, true, false, false));
        } else if (infectionLevel >= 24000) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, infectionLevel, 0, true, false, false));
        } else if (infectionLevel >= 12000) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, infectionLevel, 0, true, false, false));
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

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
