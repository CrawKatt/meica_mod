package com.example.examplemod.entity.custom.brotecito;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BrotecitoAttackGoal extends Goal {
    private final BrotecitoEntity brotecito;
    private int attackTimer;
    private final double speedModifier;
    private int seeTime;
    private final float attackRadius;
    private final float attackRadiusSqr;

    public BrotecitoAttackGoal(BrotecitoEntity brotecito) {
        this.brotecito = brotecito;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        this.speedModifier = 1.0D;
        this.attackRadius = 2.0F; // Ajusta el radio de ataque según sea necesario
        this.attackRadiusSqr = this.attackRadius * this.attackRadius;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.brotecito.getTarget();
        return target != null && this.brotecito.canAttack(target) && this.brotecito.getMoveControl() instanceof BrotecitoMoveControl;
    }

    @Override
    public void start() {
        this.attackTimer = 0;
        this.seeTime = 0;
        super.start();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.brotecito.getTarget();
        return target != null && this.brotecito.canAttack(target) && !this.brotecito.isOrderedToSit();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.brotecito.getTarget();
        if (target != null) {
            double distanceToTargetSqr = this.brotecito.distanceToSqr(target.getX(), target.getY(), target.getZ());

            boolean canSeeTarget = this.brotecito.getSensing().hasLineOfSight(target);
            if (canSeeTarget) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }

            if (distanceToTargetSqr <= (double)this.brotecito.getBbWidth() * this.brotecito.getBbWidth()) {
                this.brotecito.getNavigation().stop();
                this.attack(target);
            } else {
                this.brotecito.getNavigation().moveTo(target, this.speedModifier);
            }

            this.brotecito.lookAt(target, 30.0F, 30.0F);
            this.brotecito.getLookControl().setLookAt(target, 30.0F, 30.0F);
        }
    }

    private void attack(LivingEntity target) {
        if (--this.attackTimer <= 0) {
            this.attackTimer = 20;
            this.brotecito.dealDamage(target);
        }
    }
}
