package com.crawkatt.meicamod.entity.projectile;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class IceArrow extends ArrowEntity {
    public IceArrow(World world, LivingEntity shooter) {
        super(world, shooter);
    }

    public IceArrow(EntityType<? extends ArrowEntity> pEntityType, World world) {
        super(pEntityType, world);
    }

    @Override
    protected void onCollision(@NotNull HitResult target) {
        super.onCollision(target);

        // Verifica si el nivel es del lado del servidor
        if (!this.getWorld().isClient) {
            // Verifica si el HitResult es una entidad (EntityHitResult)
            if (target instanceof EntityHitResult entityHitResult) {
                // Verifica si la entidad golpeada es un jugador o mob
                if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                    // Aplica el efecto de congelación visual
                    if (livingEntity instanceof PlayerEntity player) {
                        // Aplica 100 ticks de congelación (5 segundos)
                        player.setFrozenTicks(100);
                    }

                    // Genera nieve en el suelo debajo del jugador o mob
                    ServerWorld serverWorld = (ServerWorld) this.getWorld();
                    BlockPos hitPos = new BlockPos((int) livingEntity.getX(), (int) livingEntity.getY(), (int) livingEntity.getZ());

                    // Intenta generar un bloque de nieve en la posición impactada
                    if (serverWorld.getBlockState(hitPos.down()).isAir()) {
                        serverWorld.setBlockState(hitPos.down(), Blocks.SNOW.getDefaultState(), 3);
                    }
                }
            } else {
                // Si la flecha impacta en el suelo, genera un bloque de nieve en el punto de impacto
                BlockPos hitPos = new BlockPos((int) target.getPos().x, (int) target.getPos().y, (int) target.getPos().z);
                ServerWorld serverWorld = (ServerWorld) this.getWorld();

                if (serverWorld.getBlockState(hitPos.down()).isAir()) {
                    serverWorld.setBlockState(hitPos.down(), Blocks.SNOW.getDefaultState(), 3);
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
    }
}
