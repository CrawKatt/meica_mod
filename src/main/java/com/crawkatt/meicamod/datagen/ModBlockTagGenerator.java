package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

// Clase ModBlockTagGenerator
// Se encarga de generar las etiquetas de los bloques y de añadirles las propiedades necesarias
// como la posibilidad de ser minados con un pico y la necesidad de una herramienta de diamante
public class ModBlockTagGenerator extends BlockTagsProvider {
    // Método constructor (Equivalente a ModBlockTagGenerator::new() en Rust)
    // La keyword `super` se usa para llamar al método constructor de la clase padre
    // En este caso, se llama al método constructor de BlockTagsProvider
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MeicaMod.MODID, existingFileHelper);
    }

    // Asegura que los bloques se puedan minar con un pico y que necesiten una herramienta de diamante
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.BROTENITA_BLOCK.get(),
                        ModBlocks.RAW_BROTENITA_BLOCK.get(),
                        ModBlocks.BROTENITA_STAIRS.get(),
                        ModBlocks.BROTENITA_SLAB.get(),
                        ModBlocks.BROTENITA_FENCE.get(),
                        ModBlocks.BROTENITA_FENCE_GATE.get(),
                        ModBlocks.BROTENITA_WALL.get(),
                        ModBlocks.BROTENITA_DOOR.get(),
                        ModBlocks.BROTENITA_TRAPDOOR.get(),
                        ModBlocks.BROTENITA_BUTTON.get(),
                        ModBlocks.BROTENITA_PRESSURE_PLATE.get(),
                        ModBlocks.RAW_BROTENITA.get(),
                        ModBlocks.BROTENITA_CROP.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.BROTENITA_BLOCK.get())
                .add(ModBlocks.RAW_BROTENITA_BLOCK.get())
                .add(ModBlocks.BROTENITA_STAIRS.get())
                .add(ModBlocks.BROTENITA_SLAB.get())
                .add(ModBlocks.BROTENITA_FENCE.get())
                .add(ModBlocks.BROTENITA_FENCE_GATE.get())
                .add(ModBlocks.BROTENITA_WALL.get())
                .add(ModBlocks.BROTENITA_DOOR.get())
                .add(ModBlocks.BROTENITA_TRAPDOOR.get())
                .add(ModBlocks.BROTENITA_BUTTON.get())
                .add(ModBlocks.BROTENITA_PRESSURE_PLATE.get())
                .add(ModBlocks.RAW_BROTENITA.get())
                .add(ModBlocks.BROTENITA_CROP.get());

        this.tag(BlockTags.FENCES)
                .add(ModBlocks.BROTENITA_FENCE.get());

        this.tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.BROTENITA_FENCE_GATE.get());

        this.tag(BlockTags.WALLS)
                .add(ModBlocks.BROTENITA_WALL.get());

        this.tag(BlockTags.DOORS)
                .add(ModBlocks.BROTENITA_DOOR.get());

        this.tag(BlockTags.TRAPDOORS)
                .add(ModBlocks.BROTENITA_TRAPDOOR.get());

        this.tag(BlockTags.BUTTONS)
                .add(ModBlocks.BROTENITA_BUTTON.get());

        this.tag(BlockTags.PRESSURE_PLATES)
                .add(ModBlocks.BROTENITA_PRESSURE_PLATE.get());

        this.tag(BlockTags.SLABS)
                .add(ModBlocks.BROTENITA_SLAB.get());
    }
}
