package com.crawkatt.meicamod.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
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

        if (!this.getWorld().isClient) {
            ServerWorld serverWorld = (ServerWorld) this.getWorld();
            Vec3d hitPos = target.getPos();

            serverWorld.spawnParticles(
                    ParticleTypes.SNOWFLAKE,
                    hitPos.x,
                    hitPos.y,
                    hitPos.z,
                    25,
                    0.2, 0.2, 0.2,
                    0.01
            );

            if (target instanceof EntityHitResult entityHitResult) {
                if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                    if (livingEntity instanceof PlayerEntity player) {
                        player.setFrozenTicks(100);
                    }

                    serverWorld.spawnParticles(
                            ParticleTypes.SNOWFLAKE,
                            livingEntity.getX(),
                            livingEntity.getBodyY(0.5),
                            livingEntity.getZ(),
                            20,
                            0.5, 0.5, 0.5,
                            0.02
                    );
                }
            }
        }
    }
}