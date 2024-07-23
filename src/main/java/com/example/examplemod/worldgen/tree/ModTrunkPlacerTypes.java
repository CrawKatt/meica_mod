package com.example.examplemod.worldgen.tree;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.worldgen.tree.custom.BigOakTrunkPlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTrunkPlacerTypes {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER =
            DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, ExampleMod.MODID);

    public static final RegistryObject<TrunkPlacerType<BigOakTrunkPlacer>> BIG_OAK_TRUNK_PLACER =
            TRUNK_PLACER.register("big_oak_trunk_placer", () -> new TrunkPlacerType<>(BigOakTrunkPlacer.CODEC));

    public static void register(IEventBus eventBus) {
        TRUNK_PLACER.register(eventBus);
    }
}
