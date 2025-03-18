package com.crawkatt.meicamod.entity.custom.meica;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class AvoidPlayerWhileCamouflagedGoal extends Goal {
    private enum State {
        IDLE,
        AVOIDING,
        ATTACKING
    }

    private final Monster entity;
    private final double farSpeed;
    private final double nearSpeed;
    private final double avoidDistance;
    private final Level level;

    private Player closestPlayer;
    private int ticksSinceLastSeen;
    private State currentState = State.IDLE;

    public AvoidPlayerWhileCamouflagedGoal(Monster entity, double farSpeed, double nearSpeed, double avoidDistance) {
        this.entity = entity;
        this.farSpeed = farSpeed;
        this.nearSpeed = nearSpeed;
        this.avoidDistance = avoidDistance;
        this.level = entity.level();
        this.ticksSinceLastSeen = 0;

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!(entity instanceof MeicaEntity meica) || !meica.isCamouflaged()) {
            return false;
        }

        this.closestPlayer = this.level.getNearestPlayer(this.entity, avoidDistance);
        if (this.closestPlayer != null && this.entity.distanceToSqr(this.closestPlayer) < avoidDistance * avoidDistance) {
            this.currentState = State.AVOIDING;
            return true;
        }

        return false;
    }

    @Override
    public void tick() {
        if (this.closestPlayer == null || !this.closestPlayer.isAlive()) {
            this.currentState = State.IDLE;
            return;
        }

        double distanceSquared = this.entity.distanceToSqr(this.closestPlayer);

        switch (this.currentState) {
            case AVOIDING:
                handleAvoiding(distanceSquared);
                break;
            case ATTACKING:
                handleAttacking(distanceSquared);
                break;
            case IDLE:
            default:
                break;
        }

        this.ticksSinceLastSeen++;
    }

    @Override
    public boolean canContinueToUse() {
        return (entity instanceof MeicaEntity meica && meica.isCamouflaged())
                && this.closestPlayer != null
                && this.entity.distanceToSqr(this.closestPlayer) < avoidDistance * avoidDistance;
    }

    private void handleAvoiding(double distanceSquared) {
        if (distanceSquared < 16.0D) {
            this.currentState = State.ATTACKING;
            return;
        }

        Vec3 escapePos = getEscapePos();
        if (escapePos != null) {
            this.entity.getNavigation().moveTo(escapePos.x, escapePos.y, escapePos.z, farSpeed);
        }
    }

    private void handleAttacking(double distanceSquared) {
        if (this.entity instanceof MeicaEntity meica) {
            RangedBowAttackGoal<MeicaEntity> attackGoal = new RangedBowAttackGoal<>(meica, nearSpeed, 20, 15.0F);
            attackGoal.tick();
        }

        if (distanceSquared >= 16.0D) {
            this.currentState = State.AVOIDING;
        }
    }

    private Vec3 getEscapePos() {
        return LandRandomPos.getPosAway(this.entity, 16, 7, this.closestPlayer.position());
    }
}