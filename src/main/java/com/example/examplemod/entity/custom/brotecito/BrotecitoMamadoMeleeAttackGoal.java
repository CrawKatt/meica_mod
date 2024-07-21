package com.example.examplemod.entity.custom.brotecito;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class BrotecitoMamadoMeleeAttackGoal extends MeleeAttackGoal {
    private final BrotecitoMamadoEntity brotecitoMamadoEntity;

    public BrotecitoMamadoMeleeAttackGoal(BrotecitoMamadoEntity brotecitoMamadoEntity, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(brotecitoMamadoEntity, speedModifier, followingTargetEvenIfNotSeen);
        this.brotecitoMamadoEntity = brotecitoMamadoEntity;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double attackReachSqr = this.getAttackReachSqr(enemy);
        if (distToEnemySqr <= attackReachSqr && this.getTicksUntilNextAttack() <= 0) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(enemy);
            brotecitoMamadoEntity.startAttack(); // Iniciar ataque
        }
    }

    @Override
    public void stop() {
        super.stop();
        brotecitoMamadoEntity.stopAttack();
    }
}
