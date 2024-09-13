package com.crawkatt.meicamod.entity.projectile;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PoisonArrow extends Arrow {
    // Constructor para la creación de la flecha
    public PoisonArrow(Level level, LivingEntity shooter) {
        super(level, shooter);
    }

    // Constructor para el registro de la flecha en ModEntities
    public PoisonArrow(EntityType<? extends Arrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity target) {
        super.doPostHurtEffects(target);

        target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
    }
}
