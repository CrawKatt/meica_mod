package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_SLAB)
                .input(ModBlocks.HOLLOW_OAK_LOG)
                .criterion(hasItem(ModBlocks.HOLLOW_OAK_LOG), conditionsFromItem(ModBlocks.HOLLOW_OAK_LOG))
                .offerTo(exporter, new Identifier(getRecipeName(Blocks.OAK_SLAB) + "_from_hollow_oak_log"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_SLAB)
                .input(ModBlocks.STRIPPED_HOLLOW_OAK_LOG)
                .criterion(hasItem(ModBlocks.STRIPPED_HOLLOW_OAK_LOG), conditionsFromItem(ModBlocks.STRIPPED_HOLLOW_OAK_LOG))
                .offerTo(exporter, new Identifier(getRecipeName(Blocks.OAK_SLAB) + "_from_stripped_hollow_oak_log"));
    }
}
