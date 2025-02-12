package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // Tags de minería y herramientas
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.BROTENITA_BLOCK)
                .add(ModBlocks.RAW_BROTENITA_BLOCK)
                .add(ModBlocks.BROTENITA_STAIRS)
                .add(ModBlocks.BROTENITA_SLAB)
                .add(ModBlocks.BROTENITA_FENCE)
                .add(ModBlocks.BROTENITA_FENCE_GATE)
                .add(ModBlocks.BROTENITA_WALL)
                .add(ModBlocks.BROTENITA_DOOR)
                .add(ModBlocks.BROTENITA_TRAPDOOR)
                .add(ModBlocks.BROTENITA_BUTTON)
                .add(ModBlocks.BROTENITA_PRESSURE_PLATE)
                .add(ModBlocks.RAW_BROTENITA)
                .add(ModBlocks.BROTENITA_CROP);

        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.BROTENITA_BLOCK)
                .add(ModBlocks.RAW_BROTENITA_BLOCK)
                .add(ModBlocks.BROTENITA_STAIRS)
                .add(ModBlocks.BROTENITA_SLAB)
                .add(ModBlocks.BROTENITA_FENCE)
                .add(ModBlocks.BROTENITA_FENCE_GATE)
                .add(ModBlocks.BROTENITA_WALL)
                .add(ModBlocks.BROTENITA_DOOR)
                .add(ModBlocks.BROTENITA_TRAPDOOR)
                .add(ModBlocks.BROTENITA_BUTTON)
                .add(ModBlocks.BROTENITA_PRESSURE_PLATE)
                .add(ModBlocks.RAW_BROTENITA)
                .add(ModBlocks.BROTENITA_CROP);

        // Tags de bloques estructurales
        getOrCreateTagBuilder(BlockTags.FENCES)
                .add(ModBlocks.BROTENITA_FENCE);

        getOrCreateTagBuilder(BlockTags.FENCE_GATES)
                .add(ModBlocks.BROTENITA_FENCE_GATE);

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(ModBlocks.BROTENITA_WALL);

        getOrCreateTagBuilder(BlockTags.DOORS)
                .add(ModBlocks.BROTENITA_DOOR);

        getOrCreateTagBuilder(BlockTags.TRAPDOORS)
                .add(ModBlocks.BROTENITA_TRAPDOOR);

        getOrCreateTagBuilder(BlockTags.BUTTONS)
                .add(ModBlocks.BROTENITA_BUTTON);

        getOrCreateTagBuilder(BlockTags.PRESSURE_PLATES)
                .add(ModBlocks.BROTENITA_PRESSURE_PLATE);

        getOrCreateTagBuilder(BlockTags.SLABS)
                .add(ModBlocks.BROTENITA_SLAB);

        // Tag para herramientas personalizadas (si es necesario)
        /*
        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.BROTENITA_BLOCK)
                .add(ModBlocks.RAW_BROTENITA_BLOCK);
        */
    }
}