package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.component.PlayerInfectionState;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerInfectionEvents implements ServerPlayerEvents.CopyFrom {
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        PlayerInfectionState oldState = PlayerInfectionState.get(oldPlayer);
        PlayerInfectionState newState = PlayerInfectionState.get(newPlayer);
        newState.getInfection().copyFrom(oldState.getInfection());
    }
}
