package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.capabilities.BossData;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.meica.MeicaEntity;
import com.crawkatt.meicamod.worldgen.dimension.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DimensionEvents {
    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        if (event.getTo() == ModDimensions.MEICADIM_LEVEL_KEY) {
            ServerLevel level = (ServerLevel) player.level();
            BossData bossData = BossData.get(level);

            if (bossData.isBossDefeated()) {
                return;
            }

            BlockPos spawnPos = new BlockPos(50, 70, 50);
            spawnMeicaBoss(level, spawnPos);
        }
    }

    private static void spawnMeicaBoss(ServerLevel level, BlockPos pos) {
        AABB searchBox = new AABB(pos).inflate(50);
        boolean bossAlreadyExists = !level.getEntitiesOfClass(MeicaEntity.class, searchBox).isEmpty();

        if (!bossAlreadyExists) {
            MeicaEntity meicaBoss = ModEntities.MEICA.get().create(level);
            if (meicaBoss != null) {
                meicaBoss.moveTo(-15, 63, 17);
                level.addFreshEntity(meicaBoss);
            }
        }
    }
}
