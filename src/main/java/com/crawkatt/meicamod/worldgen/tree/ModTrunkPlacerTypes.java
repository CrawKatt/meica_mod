package com.crawkatt.meicamod.worldgen.tree;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.worldgen.tree.custom.HollowOakTrunkPlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTrunkPlacerTypes {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS =
            DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, MeicaMod.MODID);

    public static final RegistryObject<TrunkPlacerType<HollowOakTrunkPlacer>> HOLLOW_OAK_TRUNK_PLACER =
            TRUNK_PLACERS.register("hollow_oak_trunk_placer", () -> new TrunkPlacerType<>(HollowOakTrunkPlacer.CODEC));

    public static void register(IEventBus eventBus) {
        TRUNK_PLACERS.register(eventBus);
    }
}
