package com.crawkatt.meicamod.entity.goal;

import com.crawkatt.meicamod.entity.custom.MeicaEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class AvoidPlayerWhileCamouflagedGoal extends Goal {
    private final HostileEntity entity;
    private final int farSpeed;
    private final double nearSpeed;
    private final double avoidDistance;
    private final World world;

    private PlayerEntity closestPlayer;
    private int ticksSinceLastSeen;

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
        return this.closestPlayer != null && this.entity.squaredDistanceTo(this.closestPlayer) < avoidDistance * avoidDistance;
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

        if (this.entity.squaredDistanceTo(this.closestPlayer) < 16.0D) {
            BowAttackGoal<MeicaEntity> attackGoal = new BowAttackGoal<>((MeicaEntity) this.entity, nearSpeed, 20, 15.0F);
            attackGoal.tick();
        }

        // Si el jugador no está demasiado cerca, la entidad ententará escapar
        if (this.entity.squaredDistanceTo(this.closestPlayer) >= 10.0D && this.ticksSinceLastSeen > 40) {
            Vec3d escapePos = this.getEscapePos();

            if (escapePos != null) {
                this.entity.getNavigation().findPathTo(escapePos.x, escapePos.y, escapePos.z, farSpeed);
            }
        }

        this.ticksSinceLastSeen++;
    }

    @Override
    public boolean shouldContinue() {
        // Continuar evitando al jugador si aún está camuflado y no ha huido completamente
        return ((MeicaEntity) this.entity).isCamouflaged() && this.closestPlayer != null && this.entity.squaredDistanceTo(this.closestPlayer) < avoidDistance * avoidDistance;
    }

    private Vec3d getEscapePos() {
        return FuzzyTargeting.findFrom(this.entity, 16, 7, this.closestPlayer.getPos());
    }
}
