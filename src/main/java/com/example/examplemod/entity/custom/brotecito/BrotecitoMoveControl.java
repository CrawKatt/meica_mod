package com.example.examplemod.entity.custom.brotecito;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class BrotecitoMoveControl extends MoveControl {
    private float yRot;
    private int jumpDelay;
    private final BrotecitoEntity brotecito;
    private boolean isAggressive;

    public BrotecitoMoveControl(BrotecitoEntity brotecito) {
        super(brotecito);
        this.brotecito = brotecito;
        this.yRot = 180.0F * brotecito.getYRot() / (float) Math.PI;
    }

    public void setDirection(float yRot, boolean isAggressive) {
        this.yRot = yRot;
        this.isAggressive = isAggressive;
    }

    public void setWantedMovement(double speed) {
        this.speedModifier = speed;
        this.operation = Operation.MOVE_TO;
    }

    public void tick() {
        this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
        this.mob.yHeadRot = this.mob.getYRot();
        this.mob.yBodyRot = this.mob.getYRot();
        if (this.operation != Operation.MOVE_TO) {
            this.mob.setZza(0.0F);
        } else {
            this.operation = Operation.WAIT;
            if (this.mob.onGround()) {
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                if (this.jumpDelay-- <= 0) {
                    this.jumpDelay = this.brotecito.getJumpDelay();
                    if (this.isAggressive) {
                        this.jumpDelay /= 3;
                    }

                    this.brotecito.getJumpControl().jump();
                    this.brotecito.playSound(this.brotecito.getJumpSound(), this.brotecito.getSoundVolume(), this.brotecito.getSoundPitch());
                } else {
                    this.brotecito.xxa = 0.0F;
                    this.brotecito.zza = 0.0F;
                    this.mob.setSpeed(0.0F);
                }
            } else {
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            }
        }
    }
}
