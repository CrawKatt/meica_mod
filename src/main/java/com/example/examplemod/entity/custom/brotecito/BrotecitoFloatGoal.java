package com.example.examplemod.entity.custom.brotecito;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BrotecitoFloatGoal extends Goal {
    private final BrotecitoEntity brotecito;

    public BrotecitoFloatGoal(BrotecitoEntity brotecito) {
        this.brotecito = brotecito;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        brotecito.getNavigation().setCanFloat(true);
    }

    public boolean canUse() {
        return (this.brotecito.isInWater() || this.brotecito.isInLava()) && this.brotecito.getMoveControl() instanceof BrotecitoMoveControl;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if (this.brotecito.getRandom().nextFloat() < 0.8F) {
            this.brotecito.getJumpControl().jump();
        }

        MoveControl moveControl = this.brotecito.getMoveControl();
        if (moveControl instanceof BrotecitoMoveControl brotecitoMoveControl) {
            brotecitoMoveControl.setWantedMovement(1.2);
        }
    }
}

