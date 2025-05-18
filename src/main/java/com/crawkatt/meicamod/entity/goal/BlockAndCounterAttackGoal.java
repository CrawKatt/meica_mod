package com.crawkatt.meicamod.entity.goal;

import com.crawkatt.meicamod.entity.custom.PlayerCloneEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;

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
    public boolean canStart() {
        return clone.getTarget() != null && clone.getStackInHand(Hand.OFF_HAND).getItem() instanceof ShieldItem;
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
    public boolean shouldContinue() {
        return currentState != null && currentState.shouldContinue();
    }

    @Override
    public void stop() {
        clone.stopUsingItem();
        currentState = null;
    }

    private class BlockingState implements State {
        private int cooldown = 20;

        @Override
        public void start() {
            clone.setCurrentHand(Hand.OFF_HAND);
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
                clone.swingHand(Hand.MAIN_HAND);
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