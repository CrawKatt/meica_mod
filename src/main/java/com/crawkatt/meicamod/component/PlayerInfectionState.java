package com.crawkatt.meicamod.component;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

public class PlayerInfectionState extends PersistentState {
    private final PlayerInfection infection = new PlayerInfection();

    public static PlayerInfectionState get(ServerPlayerEntity player) {
        PersistentStateManager manager = player.getServerWorld().getPersistentStateManager();
        return manager.getOrCreate(PlayerInfectionState::fromNbt, PlayerInfectionState::new, "player_infection");
    }

    private static PlayerInfectionState fromNbt(NbtCompound nbt) {
        PlayerInfectionState state = new PlayerInfectionState();
        state.infection.loadNbt(nbt);
        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        infection.saveNbt(nbt);
        return nbt;
    }

    public PlayerInfection getInfection() {
        return infection;
    }
}
