package com.example.examplemod.entity.custom.brotecito;

import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BrotecitoKeepOnJumpingGoal extends Goal {
    private final BrotecitoEntity brotecito;

    public BrotecitoKeepOnJumpingGoal(BrotecitoEntity brotecito) {
        this.brotecito = brotecito;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    public boolean canUse() {
        return !this.brotecito.isPassenger();
    }

    public void tick() {
        MoveControl moveControl = this.brotecito.getMoveControl();
        if (moveControl instanceof BrotecitoMoveControl brotecitoMoveControl) {
            brotecitoMoveControl.setWantedMovement(1.0);
        }
    }
}
