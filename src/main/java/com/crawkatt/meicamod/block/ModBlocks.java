package com.crawkatt.meicamod.block;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.custom.BrotenitaCropBlock;
import com.crawkatt.meicamod.block.custom.BrotenitaMelterBlock;
import com.crawkatt.meicamod.block.custom.RawBrotenitaCluster;
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
    public static final AmethystClusterBlock BROTENITA = registerBlock("brotenita",
            new AmethystClusterBlock(7, 3, FabricBlockSettings.copyOf(Blocks.AMETHYST_CLUSTER)
                    .nonOpaque()
                    .solid()
                    .ticksRandomly()
                    .strength(1.5F)
                    .requiresTool()
                    .luminance(state -> 5)
                    .sounds(BlockSoundGroup.AMETHYST_CLUSTER)));

    public static final Block BROTENITA_BLOCK = registerBlock("brotenita_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block BROTENITA_STAIRS = registerBlock("brotenita_stairs",
            new StairsBlock(ModBlocks.BROTENITA_BLOCK.getDefaultState(), FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));

    public static final Block BROTENITA_SLAB = registerBlock("brotenita_slab",
            new SlabBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));

    public static final Block BROTENITA_BUTTON = registerBlock("brotenita_button",
            new ButtonBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), BlockSetType.IRON, 10, true));

    public static final Block BROTENITA_PRESSURE_PLATE = registerBlock("brotenita_pressure_plate",
            new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING,
                    FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), BlockSetType.IRON));

    public static final Block BROTENITA_FENCE = registerBlock("brotenita_fence",
            new FenceBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));

    public static final Block BROTENITA_FENCE_GATE = registerBlock("brotenita_fence_gate",
            new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), WoodType.ACACIA));

    public static final Block BROTENITA_WALL = registerBlock("brotenita_wall",
            new WallBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));

    public static final Block BROTENITA_DOOR = registerBlock("brotenita_door",
            new DoorBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), BlockSetType.IRON));

    public static final Block BROTENITA_TRAPDOOR = registerBlock("brotenita_trapdoor",
            new TrapdoorBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), BlockSetType.IRON));

    public static final Block RAW_BROTENITA_BLOCK = registerBlock("raw_brotenita_block",
            new Block(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)));

    public static final Block BROTENITA_MELTER = Registry.register(Registries.BLOCK, new Identifier(MeicaMod.MOD_ID, "brotenita_melter"),
            new BrotenitaMelterBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()));

    public static final Block BROTENITA_CROP = Registry.register(Registries.BLOCK, new Identifier(MeicaMod.MOD_ID, "brotenita_crop"),
            new BrotenitaCropBlock(FabricBlockSettings.copyOf(Blocks.SWEET_BERRY_BUSH)
                    .ticksRandomly()
                    .luminance(state -> 5)
                    .sounds(BlockSoundGroup.AMETHYST_CLUSTER)
                    .requiresTool()));

    public static final Block RAW_BROTENITA = Registry.register(Registries.BLOCK, new Identifier(MeicaMod.MOD_ID, "raw_brotenita"),
            new RawBrotenitaCluster(7, 3, FabricBlockSettings.copyOf(Blocks.AMETHYST_CLUSTER)
                    .nonOpaque()
                    .ticksRandomly()
                    .strength(1.5F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.AMETHYST_CLUSTER)
                    .luminance((state -> 5))));

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
