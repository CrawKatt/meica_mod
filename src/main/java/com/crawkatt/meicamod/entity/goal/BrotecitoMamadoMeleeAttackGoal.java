package com.crawkatt.meicamod.entity.goal;

import com.crawkatt.meicamod.entity.custom.BrotecitoMamadoEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.util.Hand;

public class BrotecitoMamadoMeleeAttackGoal extends MeleeAttackGoal {
    private final BrotecitoMamadoEntity brotecitoMamadoEntity;

    public BrotecitoMamadoMeleeAttackGoal(BrotecitoMamadoEntity brotecitoMamadoEntity, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(brotecitoMamadoEntity, speedModifier, followingTargetEvenIfNotSeen);
        this.brotecitoMamadoEntity = brotecitoMamadoEntity;
    }

    @Override
    protected void attack(LivingEntity enemy, double distToEnemySqr) {
        double attackReachSqr = this.getSquaredMaxAttackDistance(enemy);
        if (distToEnemySqr <= attackReachSqr && this.getCooldown() <= 0) {
            this.resetCooldown();
            this.mob.swingHand(Hand.MAIN_HAND);
            this.mob.tryAttack(enemy);
            brotecitoMamadoEntity.startAttack(); // Iniciar ataque
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
