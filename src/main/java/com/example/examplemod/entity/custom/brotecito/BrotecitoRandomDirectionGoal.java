package com.example.examplemod.entity.custom.brotecito;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BrotecitoRandomDirectionGoal extends Goal {
    private final BrotecitoEntity brotecito;
    private float chosenDegrees;
    private int nextRandomizeTime;

    public BrotecitoRandomDirectionGoal(BrotecitoEntity brotecito) {
        this.brotecito = brotecito;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    public boolean canUse() {
        return this.brotecito.getTarget() == null && (this.brotecito.onGround()
                || this.brotecito.isInWater()
                || this.brotecito.isInLava()
                || this.brotecito.hasEffect(MobEffects.LEVITATION))
                && this.brotecito.getMoveControl() instanceof BrotecitoMoveControl;
    }

    public void tick() {
        if (--this.nextRandomizeTime <= 0) {
            this.nextRandomizeTime = this.adjustedTickDelay(40 + this.brotecito.getRandom().nextInt(60));
            this.chosenDegrees = (float) this.brotecito.getRandom().nextInt(360);
        }

        MoveControl moveControl = this.brotecito.getMoveControl();
        if (moveControl instanceof BrotecitoMoveControl brotecitoMoveControl) {
            brotecitoMoveControl.setDirection(this.chosenDegrees, false);
        }
    }
}
