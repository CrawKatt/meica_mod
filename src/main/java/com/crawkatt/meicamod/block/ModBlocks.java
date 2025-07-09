package com.crawkatt.meicamod.block;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.custom.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block BROTECITO_SPROUT = registerBlock("brotecito_sprout",
            new BrotecitoSproutBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .noCollision()
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.CROP)));

    public static final Block STRIPPED_HOLLOW_OAK_LOG = registerBlock("stripped_hollow_oak_log", createStrippedHollowLogBlock(Blocks.STRIPPED_OAK_LOG));
    public static final Block HOLLOW_OAK_LOG = registerBlock("hollow_oak_log", createHollowLogBlock(STRIPPED_HOLLOW_OAK_LOG, Blocks.OAK_LOG));

    private static HollowLogBlock createStrippedHollowLogBlock(AbstractBlock counterpart) {
        return new HollowLogBlock(null, FabricBlockSettings.copy(counterpart));
    }

    private static HollowLogBlock createHollowLogBlock(Block strippedBlock, AbstractBlock counterpart) {
        return new HollowLogBlock(strippedBlock.getDefaultState(), FabricBlockSettings.copy(counterpart));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(MeicaMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(MeicaMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        MeicaMod.LOGGER.info("Registering Mod Blocks for " + MeicaMod.MOD_ID);
    }
}
