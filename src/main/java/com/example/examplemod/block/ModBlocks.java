package com.example.examplemod.block;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ExampleMod.MODID);

    public static final RegistryObject<Block> BROTENITA_BLOCK = registerBlock("brotenita_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> BROTENITA_STAIRS = registerBlock("brotenita_stairs",
            () -> new StairBlock(() -> ModBlocks.BROTENITA_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    // todo: Añadir las texturas cuando Aldo ya las tenga listas
    public static final RegistryObject<Block> BROTENITA_SLAB = registerBlock("brotenita_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> BROTENITA_BUTTON = registerBlock("brotenita_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON),
                    BlockSetType.IRON, 10, true));

    public static final RegistryObject<Block> BROTENITA_PRESSURE_PLATE = registerBlock("brotenita_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK),
                    BlockSetType.IRON));

    public static final RegistryObject<Block> BROTENITA_FENCE = registerBlock("brotenita_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> BROTENITA_FENCE_GATE = registerBlock("brotenita_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), SoundEvents.CHAIN_PLACE, SoundEvents.ANVIL_BREAK));

    public static final RegistryObject<Block> BROTENITA_WALL = registerBlock("brotenita_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> BROTENITA_DOOR = registerBlock("brotenita_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), BlockSetType.OAK)); // Necesario OAK para abrir con la mano

    public static final RegistryObject<Block> BROTENITA_TRAPDOOR = registerBlock("brotenita_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), BlockSetType.OAK)); // Necesario OAK para abrir con la mano

    public static final RegistryObject<Block> RAW_BROTENITA_BLOCK = registerBlock("raw_brotenita_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    //public static final RegistryObject<Block> BROTENITA_ORE = registerBlock("raw_brotenita_block",
            //() -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    //.strength(2F).requiresCorrectToolForDrops(), UniformInt.of(3, 6)));

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
