package com.crawkatt.meicamod.entity.goal;

import com.crawkatt.meicamod.entity.custom.MeicaEntity;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class AvoidPlayerWhileCamouflagedGoal extends Goal {
    private enum State {
        IDLE,
        AVOIDING,
        ATTACKING
    }

    private final HostileEntity entity;
    private final int farSpeed;
    private final double nearSpeed;
    private final double avoidDistance;
    private final World world;

    private PlayerEntity closestPlayer;
    private int ticksSinceLastSeen;
    private State currentState = State.IDLE;

    public AvoidPlayerWhileCamouflagedGoal(HostileEntity entity, int farSpeed, double nearSpeed, double avoidDistance) {
        this.entity = entity;
        this.farSpeed = farSpeed;
        this.nearSpeed = nearSpeed;
        this.avoidDistance = avoidDistance;
        this.world = entity.getWorld();
        this.ticksSinceLastSeen = 0;

        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (!((MeicaEntity) this.entity).isCamouflaged()) {
            return false;
        }

        this.closestPlayer = this.world.getClosestPlayer(this.entity, avoidDistance);
        if (this.closestPlayer != null && this.entity.squaredDistanceTo(this.closestPlayer) < avoidDistance * avoidDistance) {
            this.currentState = State.AVOIDING;
            return true;
        }

        return false;
    }

    @Override
    public void start() {
        // Inicia el goal cuando comienza a evitar al jugador
        this.ticksSinceLastSeen = 0;
    }

    @Override
    public void tick() {
        if (this.closestPlayer == null || !this.closestPlayer.isAlive()) {
            this.currentState = State.IDLE;
            return;
        }

        double distanceSquared = this.entity.squaredDistanceTo(this.closestPlayer);

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
    public boolean shouldContinue() {
        // Continuar evitando al jugador si aún está camuflado y no ha huido completamente
        return ((MeicaEntity) this.entity).isCamouflaged() && this.closestPlayer != null && this.entity.squaredDistanceTo(this.closestPlayer) < avoidDistance * avoidDistance;
    }

    private void handleAvoiding(double distanceSquared) {
        if (distanceSquared < 16.0D) {
            this.currentState = State.ATTACKING;
            return;
        }

        Vec3d escapePos = getEscapePos();
        if (escapePos != null) {
            this.entity.getNavigation().startMovingTo(escapePos.x, escapePos.y, escapePos.z, farSpeed);
        }
    }

    private void handleAttacking(double distanceSquared) {
        BowAttackGoal<MeicaEntity> attackGoal = new BowAttackGoal<>((MeicaEntity) this.entity, nearSpeed, 20, 15.0F);
        attackGoal.tick();

        if (distanceSquared >= 16.0D) {
            this.currentState = State.AVOIDING;
        }
    }

    private Vec3d getEscapePos() {
        return FuzzyTargeting.findFrom(this.entity, 16, 7, this.closestPlayer.getPos());
    }
}
