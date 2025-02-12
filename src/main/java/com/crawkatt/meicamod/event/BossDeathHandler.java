package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.capabilities.BossData;
import com.crawkatt.meicamod.entity.custom.MeicaEntity;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;

public class BossDeathHandler implements ServerLivingEntityEvents.AfterDeath{
    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if (entity instanceof MeicaEntity meicaBoss) {
            if (!entity.getWorld().isClient) {
                ServerWorld world = (ServerWorld) meicaBoss.getWorld();
                BossData bossData = BossData.get(world);
                bossData.setBossDefeated(true);
                bossData.setBossSpawned(false);
            }
        }
    }
}
