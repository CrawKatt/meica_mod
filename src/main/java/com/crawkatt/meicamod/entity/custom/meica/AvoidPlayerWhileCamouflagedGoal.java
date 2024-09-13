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
    private final Monster entity;
    private final double farSpeed;
    private final double nearSpeed;
    private final double avoidDistance;
    private final Level level;

    private Player closestPlayer;
    private int ticksSinceLastSeen;

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
        if (!((MeicaEntity) this.entity).isCamouflaged()) {
            return false;
        }

        this.closestPlayer = this.level.getNearestPlayer(this.entity, avoidDistance);
        return this.closestPlayer != null && this.entity.distanceToSqr(this.closestPlayer) < avoidDistance * avoidDistance;
    }

    @Override
    public void start() {
        // Inicia el goal cuando comienza a evitar al jugador
        this.ticksSinceLastSeen = 0;
    }

    @Override
    public void tick() {
        if (this.closestPlayer == null || !this.closestPlayer.isAlive()) {
            return;
        }

        if (this.entity.distanceToSqr(this.closestPlayer) < 16.0D) {
            RangedBowAttackGoal<MeicaEntity> attackGoal = new RangedBowAttackGoal<>((MeicaEntity) this.entity, nearSpeed, 20, 15.0F);
            attackGoal.tick();
        }

        // Si el jugador no está demasiado cerca, la entidad ententará escapar
        if (this.entity.distanceToSqr(this.closestPlayer) >= 10.0D && this.ticksSinceLastSeen > 40) {
            Vec3 escapePos = this.getEscapePos();

            if (escapePos != null) {
                this.entity.getNavigation().moveTo(escapePos.x, escapePos.y, escapePos.z, farSpeed);
            }
        }

        this.ticksSinceLastSeen++;
    }

    @Override
    public boolean canContinueToUse() {
        // Continuar evitando al jugador si aún está camuflado y no ha huido completamente
        return ((MeicaEntity) this.entity).isCamouflaged() && this.closestPlayer != null && this.entity.distanceToSqr(this.closestPlayer) < avoidDistance * avoidDistance;
    }

    private Vec3 getEscapePos() {
        return LandRandomPos.getPosAway(this.entity, 16, 7, this.closestPlayer.position());
    }
}