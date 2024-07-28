package com.example.examplemod.infection;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerInfectionProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<PlayerInfection> TIME_IN_BIOME = CapabilityManager.get(new CapabilityToken<PlayerInfection>() {});

    private PlayerInfection infection = null;
    private final LazyOptional<PlayerInfection> optional = LazyOptional.of(this::createPlayerInfection);

    private PlayerInfection createPlayerInfection() {
        if (this.infection == null) {
            this.infection = new PlayerInfection();
        }

        return this.infection;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == TIME_IN_BIOME) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerInfection().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerInfection().loadNBTData(nbt);
    }
}
