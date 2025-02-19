package com.crawkatt.meicamod.component;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

public class BossData extends PersistentState {
    private static final String DATA_NAME = "meica_boss_data";
    private boolean bossDefeated = false;
    private boolean bossSpawned = false;

    @Override
    public NbtCompound writeNbt(NbtCompound compoundTag) {
        compoundTag.putBoolean("BossDefeated", this.bossDefeated);
        compoundTag.putBoolean("BossSpawned", this.bossSpawned);
        return compoundTag;
    }

    public static BossData load(NbtCompound compoundTag) {
        BossData bossData = new BossData();
        bossData.bossDefeated = compoundTag.getBoolean("BossDefeated");
        bossData.bossSpawned = compoundTag.getBoolean("BossSpawned");
        return bossData;
    }

    public void setBossDefeated(boolean bossDefeated) {
        this.bossDefeated = bossDefeated;
        this.setDirty(true);
    }

    public void setBossSpawned(boolean spawned) {
        this.bossSpawned = spawned;
        this.setDirty(true);
    }

    public boolean isBossDefeated() {
        return this.bossDefeated;
    }

    public boolean isBossSpawned() {
        return bossSpawned;
    }


    public static BossData get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(BossData::load, BossData::new, DATA_NAME);
    }
}
