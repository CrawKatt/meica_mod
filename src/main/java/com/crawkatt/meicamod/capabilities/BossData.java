package com.crawkatt.meicamod.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class BossData extends SavedData {
    private static final String DATA_NAME = "meica_boss_data";
    private boolean bossDefeated = false;

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putBoolean("BossDefeated", this.bossDefeated);
        return compoundTag;
    }

    public static BossData load(CompoundTag compoundTag) {
        BossData bossData = new BossData();
        bossData.bossDefeated = compoundTag.getBoolean("BossDefeated");
        return bossData;
    }

    public void setBossDefeated(boolean bossDefeated) {
        this.bossDefeated = bossDefeated;
        this.setDirty();
    }

    public boolean isBossDefeated() {
        return this.bossDefeated;
    }

    public static BossData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(BossData::load, BossData::new, DATA_NAME);
    }
}
