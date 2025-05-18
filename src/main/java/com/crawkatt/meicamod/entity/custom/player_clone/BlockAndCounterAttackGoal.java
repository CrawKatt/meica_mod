package com.crawkatt.meicamod.entity.custom.player_clone;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ShieldItem;

public class BlockAndCounterAttackGoal extends Goal {
    private final PlayerCloneEntity clone;
    private State currentState;

    public BlockAndCounterAttackGoal(PlayerCloneEntity clone) {
        this.clone = clone;
    }

    private interface State {
        void start();
        void tick();
        boolean shouldContinue();
    }

    @Override
    public boolean canUse() {
        return clone.getTarget() != null && clone.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ShieldItem;
    }

    @Override
    public void start() {
        currentState = new BlockingState();
        currentState.start();
    }

    @Override
    public void tick() {
        if (currentState != null) {
            currentState.tick();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return currentState != null && currentState.shouldContinue();
    }

    @Override
    public void stop() {
        clone.stopUsingItem();
    }

    private class BlockingState implements State {
        private int cooldown = 20;

        @Override
        public void start() {
            clone.startUsingItem(InteractionHand.OFF_HAND);
        }

        @Override
        public void tick() {
            cooldown--;
            if (cooldown <= 0) {
                // Transición al estado de contraataque
                currentState = new CounterAttackState();
                currentState.start();
            }
        }

        @Override
        public boolean shouldContinue() {
            return true;
        }
    }

    private class CounterAttackState implements State {
        @Override
        public void start() {
            clone.stopUsingItem();
            if (clone.getTarget() != null) {
                clone.swing(InteractionHand.MAIN_HAND);
            }
        }

        @Override
        public void tick() {}

        @Override
        public boolean shouldContinue() {
            return false;
        }
    }
}
