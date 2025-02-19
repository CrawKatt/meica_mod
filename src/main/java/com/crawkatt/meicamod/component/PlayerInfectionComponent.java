package com.crawkatt.meicamod.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class PlayerInfectionComponent implements AutoSyncedComponent {
    private int infection = 0;
    private static final int MAX_INFECTION = 72000;

    public int getInfection() {
        return infection;
    }

    public void setInfection(int infectionLevel) {
        infection = infectionLevel;
    }

    public void addInfection(int amount, PlayerEntity player) {
        infection = Math.min(infection + amount, MAX_INFECTION);
        ModComponents.INFECTION.sync(player);
    }

    public void subtractInfection(int amount, PlayerEntity player) {
        infection = Math.max(infection - amount, 0);
        ModComponents.INFECTION.sync(player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        infection = tag.getInt("infection");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("infection", infection);
    }
}