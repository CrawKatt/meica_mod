package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.HOLLOW_OAK_LOG);
        addDrop(ModBlocks.STRIPPED_HOLLOW_OAK_LOG);
        addDrop(ModBlocks.MEICA_TEDDY);
        addDrop(ModBlocks.ZERO_FIVE_TEDDY);
    }
}
