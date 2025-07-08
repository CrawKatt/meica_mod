package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.block.custom.BrotenitaCropBlock;
import com.crawkatt.meicamod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.BROTENITA);
        addDrop(ModBlocks.BROTENITA, brotenitaOreWithBonusDrops(ModBlocks.BROTENITA, ModBlocks.BROTENITA.asItem(), ModItems.SMALL_BROTENITA));
        addDrop(ModBlocks.RAW_BROTENITA_BLOCK, copperLikeOreDrops(ModBlocks.RAW_BROTENITA_BLOCK, ModBlocks.BROTENITA.asItem()));

        addDrop(ModBlocks.BROTENITA_STAIRS);
        addDrop(ModBlocks.BROTENITA_TRAPDOOR);
        addDrop(ModBlocks.BROTENITA_WALL);
        addDrop(ModBlocks.BROTENITA_FENCE);
        addDrop(ModBlocks.BROTENITA_FENCE_GATE);
        addDrop(ModBlocks.BROTENITA_BUTTON);
        addDrop(ModBlocks.BROTENITA_PRESSURE_PLATE);
        addDrop(ModBlocks.BROTENITA_MELTER);
        addDrop(ModBlocks.MEICA_TEDDY);
        addDrop(ModBlocks.ZERO_FIVE_TEDDY);
        addDrop(ModBlocks.HOLLOW_OAK_LOG);
        addDrop(ModBlocks.STRIPPED_HOLLOW_OAK_LOG);

        addDrop(ModBlocks.BROTENITA_DOOR, doorDrops(ModBlocks.BROTENITA_DOOR));
        addDrop(ModBlocks.BROTENITA_SLAB, slabDrops(ModBlocks.BROTENITA_SLAB));

        BlockStatePropertyLootCondition.Builder builder = BlockStatePropertyLootCondition.builder(ModBlocks.BROTENITA_CROP).properties(StatePredicate.Builder.create()
                .exactMatch(BrotenitaCropBlock.AGE, 5));
        addDrop(ModBlocks.BROTENITA_CROP, cropDrops(ModBlocks.BROTENITA_CROP, ModBlocks.BROTENITA.asItem(), ModItems.SMALL_BROTENITA, builder));
    }

    public LootTable.Builder copperLikeOreDrops(Block drop, Item item) {
        return BlockLootTableGenerator.dropsWithSilkTouch(drop, this.applyExplosionDecay(drop,
                ((LeafEntry.Builder<?>)
                        ItemEntry.builder(item)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 5.0f))))
                                .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))));
    }

    public LootTable.Builder brotenitaOreWithBonusDrops(Block block, Item primaryItem, Item secondaryItem) {
        return BlockLootTableGenerator.dropsWithSilkTouch(block,
                        this.applyExplosionDecay(block, ItemEntry.builder(block)))
                .pool(LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0f, 1.0f))
                        .with(ItemEntry.builder(primaryItem)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f))))
                )
                .pool(LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0f, 1.0f))
                        .with(ItemEntry.builder(secondaryItem)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f))))
                );
    }
}
