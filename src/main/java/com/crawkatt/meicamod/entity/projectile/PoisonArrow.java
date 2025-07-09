package com.crawkatt.meicamod.entity.projectile;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class PoisonArrow extends ArrowEntity {
    public PoisonArrow(World world, LivingEntity shooter) {
        super(world, shooter);
    }

    public PoisonArrow(EntityType<? extends ArrowEntity> pEntityType, World world) {
        super(pEntityType, world);
    }

    @Override
    protected void onCollision(@NotNull HitResult target) {
        super.onCollision(target);

        if (!this.getWorld().isClient) {
            ServerWorld serverWorld = (ServerWorld) this.getWorld();
            Vec3d hitPos = target.getPos();

            serverWorld.spawnParticles(
                    ParticleTypes.SPORE_BLOSSOM_AIR,
                    hitPos.x,
                    hitPos.y,
                    hitPos.z,
                    25,
                    0.4, 0.4, 0.4,
                    0.01
            );

            if (target instanceof EntityHitResult entityHitResult) {
                if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                    AreaEffectCloudEntity areaEffectCloud = new AreaEffectCloudEntity(serverWorld, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                    areaEffectCloud.setRadius(5);
                    areaEffectCloud.setWaitTime(15);
                    areaEffectCloud.setDuration(300);
                    areaEffectCloud.addEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 1));
                    this.getWorld().spawnEntity(areaEffectCloud);

                    serverWorld.spawnParticles(
                            ParticleTypes.SPORE_BLOSSOM_AIR,
                            livingEntity.getX(),
                            livingEntity.getBodyY(0.5),
                            livingEntity.getZ(),
                            15,
                            0.5, 0.5, 0.5,
                            0.02
                    );
                }
            }
        }
    }
}