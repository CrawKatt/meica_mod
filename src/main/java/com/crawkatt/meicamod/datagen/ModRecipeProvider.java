package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.datagen.custom.BrotenitaMelterRecipeBuilder;
import com.crawkatt.meicamod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BROTENITA_BLOCK.get())
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BROTENITA_SWORD.get())
                .pattern("B")
                .pattern("B")
                .pattern("S")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BROTENITA_PICKAXE.get())
                .pattern("BBB")
                .pattern(" S ")
                .pattern(" S ")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BROTENITA_AXE.get())
                .pattern("BB")
                .pattern("BS")
                .pattern(" S")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BROTENITA_SHOVEL.get())
                .pattern("B")
                .pattern("S")
                .pattern("S")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROTENITA_STAIRS.get(), 4)
                .pattern("  B")
                .pattern(" BB")
                .pattern("BBB")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROTENITA_SLAB.get(), 6)
                .pattern("BBB")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROTENITA_WALL.get(), 6)
                .pattern(" B ")
                .pattern("BBB")
                .pattern("B B")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROTENITA_FENCE.get(), 3)
                .pattern("BSB")
                .pattern("BSB")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROTENITA_FENCE_GATE.get())
                .pattern("SBS")
                .pattern("SBS")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BROTENITA_HOE.get())
                .pattern("BB")
                .pattern(" S")
                .pattern(" S")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BROTENITA_STAFF.get())
                .pattern(" C ")
                .pattern(" S ")
                .pattern(" S ")
                .define('C', ModItems.RAW_BROTENITA.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.RAW_BROTENITA.get()), has(ModItems.RAW_BROTENITA.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.BROTENITA_DOOR.get())
                .pattern("BB")
                .pattern("BB")
                .pattern("BB")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.BROTENITA_TRAPDOOR.get())
                .pattern("BBB")
                .pattern("BBB")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.BROTENITA_PRESSURE_PLATE.get())
                .pattern("BB")
                .define('B', ModItems.BROTENITA_INGOT.get())
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ModBlocks.BROTENITA_BUTTON.get())
                .requires(ModItems.BROTENITA_INGOT.get())
                .unlockedBy(getHasName(ModItems.BROTENITA_INGOT.get()), has(ModItems.BROTENITA_INGOT.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BROTENITA_MEAL.get(), 1)
                .requires(ModItems.RAW_BROTENITA.get())
                .unlockedBy(getHasName(ModItems.RAW_BROTENITA.get()), has(ModItems.RAW_BROTENITA.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BROTENITA_INGOT.get(), 9)
                .requires(ModBlocks.BROTENITA_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.BROTENITA_BLOCK.get()), has(ModBlocks.BROTENITA_BLOCK.get()))
                .save(pWriter);

        new BrotenitaMelterRecipeBuilder(ModItems.RAW_BROTENITA.get(), ModItems.BROTENITA_INGOT.get(), 1)
                .unlockedBy("has_raw_brotenita", has(ModItems.RAW_BROTENITA.get()))
                .save(pWriter, MeicaMod.MODID);
    }
}
