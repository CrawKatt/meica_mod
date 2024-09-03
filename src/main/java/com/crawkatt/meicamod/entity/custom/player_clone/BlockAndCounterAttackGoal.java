package com.crawkatt.meicamod.entity.custom.player_clone;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ShieldItem;

public class BlockAndCounterAttackGoal extends Goal {
    private final PlayerCloneEntity clone;
    private int blockCooldown;
    private boolean isBlocking;

    public BlockAndCounterAttackGoal(PlayerCloneEntity clone) {
        this.clone = clone;
        this.isBlocking = false;
    }

    @Override
    public boolean canUse() {
        // Activar solo si el clon tiene un escudo equipado y está siendo atacado
        return clone.getTarget() != null && clone.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ShieldItem;
    }

    @Override
    public void start() {
        // Levantar el escudo
        clone.startUsingItem(InteractionHand.OFF_HAND);
        blockCooldown = 20; // Tiempo para mantener el escudo levantado antes de contraatacar
        isBlocking = true;
    }

    @Override
    public void tick() {
        if (isBlocking) {
            if (blockCooldown > 0) {
                blockCooldown--;
            } else {
                // Detener el bloqueo y contraatacar
                clone.stopUsingItem();
                isBlocking = false;

                if (clone.getTarget() != null) {
                    clone.swing(InteractionHand.MAIN_HAND); // Contraatacar
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        // Continuar usando esta meta mientras esté bloqueando
        return isBlocking;
    }

    @Override
    public void stop() {
        // Asegurarse de que se detenga cualquier acción de bloqueo si esta meta se detiene
        clone.stopUsingItem();
        isBlocking = false;
    }
}
