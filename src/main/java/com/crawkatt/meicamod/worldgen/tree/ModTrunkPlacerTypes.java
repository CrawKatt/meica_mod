package com.crawkatt.meicamod.worldgen.tree;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.mixin.TrunkPlacerTypeInvoker;
import com.crawkatt.meicamod.worldgen.tree.custom.HollowOakTrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

public class ModTrunkPlacerTypes {
    public static final TrunkPlacerType<?> HOLLOW_OAK_TRUNK_PLACER =  TrunkPlacerTypeInvoker.callRegister("hollow_oak_trunk_placer",
            HollowOakTrunkPlacer.CODEC);

    public static void register() {
        MeicaMod.LOGGER.info("Registering Trunk Placer for " + MeicaMod.MOD_ID);
    }
}
