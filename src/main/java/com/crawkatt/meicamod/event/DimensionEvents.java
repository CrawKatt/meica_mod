package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.capabilities.BossData;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.MeicaEntity;
import com.crawkatt.meicamod.worldgen.dimension.ModDimensions;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;

public class DimensionEvents implements ServerEntityWorldChangeEvents.AfterPlayerChange {
    @Override
    public void afterChangeWorld(ServerPlayerEntity player, ServerWorld origin, ServerWorld destination) {
        if (destination.getRegistryKey() == ModDimensions.MEICADIM_WORLD_KEY) {
            teleportPlayerToFixedPosition(player, destination);
            BossData bossData = BossData.get(destination);

            if (!bossData.isBossDefeated() && !bossData.isBossSpawned()) {
                spawnMeicaBoss(destination, new BlockPos(0, 78, 0));
                bossData.setBossSpawned(true);
            }
        }
    }

    private static void teleportPlayerToFixedPosition(ServerPlayerEntity player, ServerWorld world) {
        BlockPos spawnPos = new BlockPos(0, 78, 0);
        player.teleport(world, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, player.getYaw(), player.getPitch());
    }

    private static void spawnMeicaBoss(ServerWorld world, BlockPos pos) {
        BossData bossData = BossData.get(world);
        if (bossData.isBossDefeated()) return;

        BlockPos bossSpawnPos = new BlockPos(-15, 63, 17);

        ChunkPos chunkPos = new ChunkPos(bossSpawnPos);
        world.getChunkManager().getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, true);

        Box searchBox = new Box(pos).expand(50);
        boolean bossAlreadyExists = !world.getEntitiesByClass(MeicaEntity.class, searchBox, EntityPredicates.EXCEPT_SPECTATOR).isEmpty();

        if (!bossAlreadyExists) {
            MeicaEntity meicaBoss = ModEntities.MEICA.create(world);
            if (meicaBoss != null) {
                meicaBoss.refreshPositionAndAngles(bossSpawnPos.getX(), bossSpawnPos.getY(), bossSpawnPos.getZ(), 0, 0);
                world.spawnEntity(meicaBoss);
            }
        }
    }
}
