package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.entity.custom.MeicaEntity;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

public class MeicaEvents implements ServerLivingEntityEvents.AfterDeath {
    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if (damageSource.getAttacker() instanceof MeicaEntity meica && entity instanceof PlayerEntity) {
            meica.playKillSound();
        }
    }
}
