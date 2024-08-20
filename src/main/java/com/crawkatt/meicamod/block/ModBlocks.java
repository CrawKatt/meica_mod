package com.crawkatt.meicamod.block;

import com.crawkatt.meicamod.MeicaMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    //public static final Block RAW_BROTENITA = registerBlock("raw_brotenita",
            //new Block(FabricBlockSettings.copyOf(Blocks.AMETHYST_CLUSTER).sounds(BlockSoundGroup.AMETHYST_CLUSTER)));
    public static final AmethystClusterBlock RAW_BROTENITA = registerBlock("raw_brotenita",
            new AmethystClusterBlock(7, 3, FabricBlockSettings.copyOf(Blocks.AMETHYST_CLUSTER)
                    .nonOpaque()
                    .solid()
                    .ticksRandomly()
                    .strength(1.5F)
                    .requiresTool()
                    .luminance(state -> 5)
                    .sounds(BlockSoundGroup.AMETHYST_CLUSTER)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(MeicaMod.MOD_ID, name), block);
    }

    private static AmethystClusterBlock registerBlock(String name, AmethystClusterBlock block) {
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
