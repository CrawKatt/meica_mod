package com.crawkatt.meicamod.capabilities;

import net.minecraft.nbt.CompoundTag;

public class PlayerInfection {
    private int infection;
    private final int MIN_INFECTION = 0;
    private final int MAX_INFECTION = 1200;

    public int getInfection() {
        return infection;
    }

    public void addInfection(int add) {
        this.infection = Math.min(infection + add, MAX_INFECTION);
    }

    public void subInfection(int sub) {
        this.infection = Math.max(infection - sub, MIN_INFECTION);
    }

    public void setInfection(int infection) {
        this.infection = Math.max(MIN_INFECTION, Math.min(infection, MAX_INFECTION));
    }

    public void copyFrom(PlayerInfection source) {
        this.infection = source.infection;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("infection", infection);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.infection = nbt.getInt("infection");
    }
}
