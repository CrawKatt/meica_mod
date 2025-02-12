package com.crawkatt.meicamod.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class PoisonArrow extends ArrowEntity {
    // Constructor para la creación de la flecha
    public PoisonArrow(World world, LivingEntity shooter) {
        super(world, shooter);
    }

    // Constructor para el registro de la flecha en ModEntities
    public PoisonArrow(EntityType<? extends ArrowEntity> pEntityType, World world) {
        super(pEntityType, world);
    }

    @Override
    protected void onHit(@NotNull LivingEntity target) {
        super.onHit(target);

        target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 1));
    }
}
