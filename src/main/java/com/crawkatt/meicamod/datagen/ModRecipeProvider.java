package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.item.ModItems;
import com.crawkatt.meicamod.recipe.BrotenitaMelterRecipeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.BROTENITA_BLOCK) // Bloque de salida al craftear
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .input('B', ModItems.BROTENITA_INGOT) // Item/Bloque de entrada al craftear
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.BROTENITA_BLOCK) + "_")); // Bloque de salida al craftear

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.BROTENITA_SWORD)
                .pattern("B")
                .pattern("B")
                .pattern("S")
                .input('B', ModItems.BROTENITA_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.BROTENITA_SWORD) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.BROTENITA_PICKAXE)
                .pattern("BBB")
                .pattern(" S ")
                .pattern(" S ")
                .input('B', ModItems.BROTENITA_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.BROTENITA_PICKAXE) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.BROTENITA_AXE)
                .pattern("BB")
                .pattern("BS")
                .pattern(" S")
                .input('B', ModItems.BROTENITA_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.BROTENITA_AXE) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.BROTENITA_SHOVEL)
                .pattern("B")
                .pattern("S")
                .pattern("S")
                .input('B', ModItems.BROTENITA_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.BROTENITA_SHOVEL) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROTENITA_STAIRS, 4)
                .pattern("  B")
                .pattern(" BB")
                .pattern("BBB")
                .input('B', ModItems.BROTENITA_INGOT)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.BROTENITA_STAIRS) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROTENITA_SLAB, 6)
                .pattern("BBB")
                .input('B', ModItems.BROTENITA_INGOT)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.BROTENITA_SLAB) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROTENITA_WALL, 6)
                .pattern(" B ")
                .pattern("BBB")
                .pattern("B B")
                .input('B', ModItems.BROTENITA_INGOT)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.BROTENITA_WALL) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROTENITA_FENCE, 3)
                .pattern("BSB")
                .pattern("BSB")
                .input('B', ModItems.BROTENITA_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.BROTENITA_FENCE) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROTENITA_FENCE_GATE)
                .pattern("SBS")
                .pattern("SBS")
                .input('B', ModItems.BROTENITA_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.BROTENITA_FENCE_GATE) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.BROTENITA_HOE)
                .pattern("BB")
                .pattern(" S")
                .pattern(" S")
                .input('B', ModItems.BROTENITA_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.BROTENITA_HOE) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.BROTENITA_STAFF)
                .pattern(" C ")
                .pattern(" S ")
                .pattern(" S ")
                .input('C', ModBlocks.BROTENITA)
                .input('S', Items.STICK)
                .criterion(hasItem(ModBlocks.BROTENITA), conditionsFromItem(ModBlocks.BROTENITA))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.BROTENITA_STAFF) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.BROTENITA_DOOR)
                .pattern("BB")
                .pattern("BB")
                .pattern("BB")
                .input('B', ModItems.BROTENITA_INGOT)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.BROTENITA_DOOR) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.BROTENITA_TRAPDOOR)
                .pattern("BBB")
                .pattern("BBB")
                .input('B', ModItems.BROTENITA_INGOT)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.BROTENITA_TRAPDOOR) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.BROTENITA_PRESSURE_PLATE)
                .pattern("BB")
                .input('B', ModItems.BROTENITA_INGOT)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.BROTENITA_PRESSURE_PLATE) + "_"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.BROTENITA_MELTER)
                .pattern("LLL")
                .pattern("FIB")
                .pattern("I I")
                .input('L', Items.POLISHED_BLACKSTONE_SLAB)
                .input('F', Items.BLAST_FURNACE)
                .input('I', Items.IRON_INGOT)
                .input('B', Items.BUCKET)
                .criterion(hasItem(Items.BLAST_FURNACE), conditionsFromItem(Items.BLAST_FURNACE))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_SLAB)
                .input(ModBlocks.HOLLOW_OAK_LOG)
                .criterion(hasItem(ModBlocks.HOLLOW_OAK_LOG), conditionsFromItem(ModBlocks.HOLLOW_OAK_LOG))
                .offerTo(exporter, new Identifier(getRecipeName(Blocks.OAK_SLAB) + "_from_hollow_oak_log"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_SLAB)
                .input(ModBlocks.STRIPPED_HOLLOW_OAK_LOG)
                .criterion(hasItem(ModBlocks.STRIPPED_HOLLOW_OAK_LOG), conditionsFromItem(ModBlocks.STRIPPED_HOLLOW_OAK_LOG))
                .offerTo(exporter, new Identifier(getRecipeName(Blocks.OAK_SLAB) + "_from_stripped_hollow_oak_log"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.BROTENITA_BUTTON)
                .input(ModItems.BROTENITA_INGOT)
                .criterion(hasItem(ModItems.BROTENITA_INGOT), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.BROTENITA_BUTTON) + "_"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.BROTENITA_MEAL, 1)
                .input(ModBlocks.BROTENITA)
                .criterion(hasItem(ModBlocks.BROTENITA), conditionsFromItem(ModBlocks.BROTENITA))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.BROTENITA_MEAL) + "_"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.BROTENITA_INGOT, 9)
                .input(ModBlocks.BROTENITA_BLOCK)
                .criterion(hasItem(ModBlocks.BROTENITA_BLOCK), conditionsFromItem(ModBlocks.BROTENITA_BLOCK))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.BROTENITA_INGOT) + "_"));

        new BrotenitaMelterRecipeBuilder(ModBlocks.BROTENITA.asItem(), ModItems.BROTENITA_INGOT, 3)
                .criterion(hasItem(ModBlocks.BROTENITA.asItem()), conditionsFromItem(ModItems.BROTENITA_INGOT))
                .offerTo(exporter);
    }
}
