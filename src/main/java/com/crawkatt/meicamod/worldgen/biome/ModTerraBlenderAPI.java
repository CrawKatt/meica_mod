package com.crawkatt.meicamod.worldgen.biome;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.util.Identifier;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

public class ModTerraBlenderAPI implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new ModOverworldRegion(new Identifier(MeicaMod.MOD_ID, "overworld"), 10));
    }
}
