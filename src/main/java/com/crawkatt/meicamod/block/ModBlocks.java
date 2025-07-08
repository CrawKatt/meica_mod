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
    public static final RawBrotenitaCluster BROTENITA = registerBlock("brotenita",
            new RawBrotenitaCluster(7, 3, FabricBlockSettings.copyOf(Blocks.AMETHYST_CLUSTER)
                    .nonOpaque()
                    .solid()
                    .ticksRandomly()
                    .strength(1.5F)
                    .requiresTool()
                    .luminance(state -> 5)
                    .sounds(BlockSoundGroup.AMETHYST_CLUSTER)));

    public static final Block BROTECITO_SPROUT = registerBlock("brotecito_sprout",
            new BrotecitoSproutBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .noCollision()
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.CROP)));

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
            new DoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_DOOR), BlockSetType.OAK));

    public static final Block BROTENITA_TRAPDOOR = registerBlock("brotenita_trapdoor",
            new TrapdoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));

    public static final Block RAW_BROTENITA_BLOCK = registerBlock("raw_brotenita_block",
            new Block(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)));

    public static final Block BROTENITA_MELTER = registerBlock("brotenita_melter",
            new BrotenitaMelterBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()));

    public static final Block BROTENITA_CROP = Registry.register(Registries.BLOCK, new Identifier(MeicaMod.MOD_ID, "brotenita_crop"),
            new BrotenitaCropBlock(FabricBlockSettings.copyOf(Blocks.SWEET_BERRY_BUSH)
                    .ticksRandomly()
                    .luminance(state -> 5)
                    .sounds(BlockSoundGroup.AMETHYST_CLUSTER)
                    .requiresTool()));

    public static final Block MEICA_TEDDY = registerBlock("meica_teddy",
            new MeicaTeddy(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()));

    public static final Block ZERO_FIVE_TEDDY = registerBlock("zero_five_teddy",
            new MeicaTeddy(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()));

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

    private static RawBrotenitaCluster registerBlock(String name, RawBrotenitaCluster block) {
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
