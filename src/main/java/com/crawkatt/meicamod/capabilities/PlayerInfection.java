package com.crawkatt.meicamod.capabilities;

import net.minecraft.nbt.CompoundTag;

public class PlayerInfection {
    private int infection = 0;
    private static final int MAX_INFECTION = 72000;

    public int getInfection() {
        return infection;
    }

    public void setInfection(int infectionLevel) {
        infection = infectionLevel;
    }

    public void addInfection(int add) {
        this.infection = Math.min(infection + add, MAX_INFECTION);
    }

    public void substractInfection(int amount) {
        this.infection = Math.max(infection - amount, 0);
    }

    public void copyFrom(PlayerInfection source) {
        this.infection = source.infection;
    }

    public void loadNBTData(CompoundTag nbt) {
        infection = nbt.getInt("infection");
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("infection", infection);
    }
}
