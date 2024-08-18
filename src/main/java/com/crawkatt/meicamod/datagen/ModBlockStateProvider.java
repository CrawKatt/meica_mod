package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.block.custom.BrotenitaCropBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MeicaMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.BROTENITA_BLOCK);
        blockWithVariants(ModBlocks.RAW_BROTENITA, "raw_brotenita");
        blockWithItem(ModBlocks.RAW_BROTENITA_BLOCK);

        stairsBlock(((StairBlock) ModBlocks.BROTENITA_STAIRS.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));
        slabBlock(((SlabBlock) ModBlocks.BROTENITA_SLAB.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));

        buttonBlock(((ButtonBlock) ModBlocks.BROTENITA_BUTTON.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));
        pressurePlateBlock(((PressurePlateBlock) ModBlocks.BROTENITA_PRESSURE_PLATE.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));
        fenceBlock(((FenceBlock) ModBlocks.BROTENITA_FENCE.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));
        fenceGateBlock(((FenceGateBlock) ModBlocks.BROTENITA_FENCE_GATE.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));
        wallBlock(((WallBlock) ModBlocks.BROTENITA_WALL.get()), blockTexture(ModBlocks.BROTENITA_BLOCK.get()));

        doorBlockWithRenderType(((DoorBlock) ModBlocks.BROTENITA_DOOR.get()), modLoc("block/brotenita_door_bottom"), modLoc("block/brotenita_door_top"), "cutout");
        trapdoorBlockWithRenderType(((TrapDoorBlock) ModBlocks.BROTENITA_TRAPDOOR.get()), modLoc("block/brotenita_trapdoor"), true, "cutout");

        makeCrop(((BrotenitaCropBlock) ModBlocks.BROTENITA_CROP.get()), "brotenita_stage", "brotenita_stage");
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    public void makeCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> states(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] states(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().cross(modelName + state.getValue(((BrotenitaCropBlock) block).getAgeProperty()),
                new ResourceLocation(MeicaMod.MODID, "block/" + textureName + state.getValue(((BrotenitaCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private void blockWithVariants(RegistryObject<Block> blockRegistryObject, String textureName) {
        ModelFile model = models().withExistingParent(blockRegistryObject.getId().getPath(), "minecraft:block/cross")
                .texture("cross", modLoc("block/" + textureName)).renderType("cutout");

        // Define variantes
        getVariantBuilder(blockRegistryObject.get())
                .forAllStates(state -> {
                    Direction direction = state.getValue(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationY(getRotationYForDirection(direction))
                            .rotationX(getRotationXForDirection(direction))
                            .build();
                });
    }

    // NO REMOVER LOS CASOS REPETIDOS
    private int getRotationXForDirection(Direction direction) {
        return switch (direction) {
            case EAST, NORTH, SOUTH, WEST -> 90;
            case DOWN -> 180;
            default -> 0;
        };
    }

    private int getRotationYForDirection(Direction direction) {
        return switch (direction) {
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            default -> 0;
        };
    }
}
