package com.crawkatt.meicamod.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class IceArrow extends Arrow {
    public IceArrow(Level level, LivingEntity shooter) {
        super(level, shooter);
    }

    public IceArrow(EntityType<? extends Arrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void onHit(@NotNull HitResult target) {
        super.onHit(target);

        // Verifica si el nivel es del lado del servidor
        if (!this.level().isClientSide) {
            // Verifica si el HitResult es una entidad (EntityHitResult)
            if (target instanceof EntityHitResult entityHitResult) {
                // Verifica si la entidad golpeada es un jugador o mob
                if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                    // Aplica el efecto de congelación visual
                    if (livingEntity instanceof Player player) {
                        // Aplica 100 ticks de congelación (5 segundos)
                        player.setTicksFrozen(100);
                    }

                    // Genera nieve en el suelo debajo del jugador o mob
                    ServerLevel serverLevel = (ServerLevel) this.level();
                    BlockPos hitPos = new BlockPos((int) livingEntity.getX(), (int) livingEntity.getY(), (int) livingEntity.getZ());

                    // Intenta generar un bloque de nieve en la posición impactada
                    if (serverLevel.getBlockState(hitPos.below()).isAir()) {
                        serverLevel.setBlock(hitPos.below(), Blocks.SNOW.defaultBlockState(), 3);
                    }
                }
            } else {
                // Si la flecha impacta en el suelo, genera un bloque de nieve en el punto de impacto
                BlockPos hitPos = new BlockPos((int) target.getLocation().x, (int) target.getLocation().y, (int) target.getLocation().z);
                ServerLevel serverLevel = (ServerLevel) this.level();

                if (serverLevel.getBlockState(hitPos.below()).isAir()) {
                    serverLevel.setBlock(hitPos.below(), Blocks.SNOW.defaultBlockState(), 3);
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
    }
}
