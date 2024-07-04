package com.example.examplemod.datagen;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.ModBlocks;
import com.example.examplemod.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    // Método constructor (Equivalente a ModBlockTagGenerator::new() en Rust)
    // La keyword `super` se usa para llamar al método constructor de la clase padre
    // En este caso, se llama al método constructor de BlockTagsProvider
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ExampleMod.MODID, existingFileHelper);
    }

    // Asegura que los bloques se puedan minar con un pico y que necesiten una herramienta de diamante
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.BROTENITA_BLOCK.get(),
                        ModBlocks.RAW_BROTENITA_BLOCK.get());

        //this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                //.add(ModBlocks.BROTENITA_ORE.get()).addTag(Tags.Blocks.ORES);

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.BROTENITA_BLOCK.get())
                .add(ModBlocks.RAW_BROTENITA_BLOCK.get());

        this.tag(BlockTags.FENCES)
                .add(ModBlocks.BROTENITA_FENCE.get());

        this.tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.BROTENITA_FENCE_GATE.get());

        this.tag(BlockTags.WALLS)
                .add(ModBlocks.BROTENITA_WALL.get());
    }
}
