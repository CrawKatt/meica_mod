package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.capabilities.BossData;
import com.crawkatt.meicamod.entity.custom.meica.MeicaEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BossDeathHandler {
    @SubscribeEvent
    public static void onBossDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof MeicaEntity meicaBoss) {
            if (!event.getEntity().level().isClientSide()) {
                ServerLevel level = (ServerLevel) meicaBoss.level();

                BossData bossData = BossData.get(level);
                bossData.setBossDefeated(true);
            }
        }
    }
}
