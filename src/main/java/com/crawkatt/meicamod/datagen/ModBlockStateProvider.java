package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MeicaMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.BROTENITA_BLOCK);
        blockWithItem(ModBlocks.RAW_BROTENITA_BLOCK);
        //blockWithItem(ModBlocks.BROTENITA_ORE); todo: Corregir esto

        stairsBlock(((StairBlock) ModBlocks.BROTENITA_STAIRS.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));
        slabBlock(((SlabBlock) ModBlocks.BROTENITA_SLAB.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));

        buttonBlock(((ButtonBlock) ModBlocks.BROTENITA_BUTTON.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));
        pressurePlateBlock(((PressurePlateBlock) ModBlocks.BROTENITA_PRESSURE_PLATE.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));
        fenceBlock(((FenceBlock) ModBlocks.BROTENITA_FENCE.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));
        fenceGateBlock(((FenceGateBlock) ModBlocks.BROTENITA_FENCE_GATE.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));
        wallBlock(((WallBlock) ModBlocks.BROTENITA_WALL.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));

        doorBlockWithRenderType(((DoorBlock) ModBlocks.BROTENITA_DOOR.get()), modLoc("block/brotenita_door_bottom"), modLoc("block/brotenita_door_top"), "cutout");
        trapdoorBlockWithRenderType(((TrapDoorBlock) ModBlocks.BROTENITA_TRAPDOOR.get()), modLoc("block/brotenita_trapdoor"), true, "cutout");
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
