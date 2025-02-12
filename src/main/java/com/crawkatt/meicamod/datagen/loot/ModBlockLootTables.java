package com.crawkatt.meicamod.datagen.loot;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.block.custom.BrotenitaCropBlock;
import com.crawkatt.meicamod.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.resource.featuretoggle.FeatureFlags;

import java.util.Set;

public class ModBlockLootTables extends BlockLootTableGenerator {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.FEATURE_MANAGER.featureSetOf());
    }

    @Override
    public void generate() {
        this.addDrop(ModBlocks.BROTENITA_BLOCK);
        this.addDrop(ModBlocks.BROTENITA_STAIRS);
        this.addDrop(ModBlocks.BROTENITA_BUTTON);
        this.addDrop(ModBlocks.BROTENITA_PRESSURE_PLATE);
        this.addDrop(ModBlocks.BROTENITA_TRAPDOOR);
        this.addDrop(ModBlocks.BROTENITA_FENCE);
        this.addDrop(ModBlocks.BROTENITA_FENCE_GATE);
        this.addDrop(ModBlocks.BROTENITA_WALL);

        this.addDrop(ModBlocks.BROTENITA_SLAB,
                block -> slabDrops(ModBlocks.BROTENITA_SLAB));
        this.addDrop(ModBlocks.BROTENITA_DOOR,
                block -> doorDrops(ModBlocks.BROTENITA_DOOR));
        this.addDrop(ModBlocks.RAW_BROTENITA,
                block -> createBrotenitaOreWithBonusDrops(ModBlocks.RAW_BROTENITA, ModItems.RAW_BROTENITA, ModItems.SMALL_BROTENITA));
        this.addDrop(ModBlocks.RAW_BROTENITA_BLOCK,
                block -> createCopperLikeOreDrops(ModBlocks.RAW_BROTENITA, ModItems.RAW_BROTENITA));

        // LootTables del Cultivo de Brotenita
        LootCondition.Builder lootItemConditionBuilder = BlockStatePropertyLootCondition.builder(ModBlocks.BROTENITA_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(BrotenitaCropBlock.AGE, 6));

        this.addDrop(ModBlocks.BROTENITA_CROP, this.cropDrops(ModBlocks.BROTENITA_CROP,
                ModItems.RAW_BROTENITA, ModItems.SMALL_BROTENITA, lootItemConditionBuilder));

        //this.addDrop(ModBlocks.BROTENITA_MELTER);
    }

    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return dropsWithSilkTouch(pBlock,
                this.applyExplosionDecay(pBlock,
                        ItemEntry.builder(item)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 5.0F)))
                                .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))));
    }

    // Crear una tabla de loot para un bloque de cultivo de Brotenita
    protected LootTable.Builder createBrotenitaOreWithBonusDrops(Block block, Item primaryItem, Item secondaryItem) {
        // Crear el primer LootItem para Raw Brotenita
        ItemEntry.Builder<?> primaryLootItem = ItemEntry.builder(primaryItem)
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)));

        // Crear el segundo LootItem para Small Brotenita
        ItemEntry.Builder<?> secondaryLootItem = ItemEntry.builder(secondaryItem)
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)));

        // Crear un LootPool y agregar el primer LootItem
        LootPool.Builder primaryLootPool = LootPool.builder()
                .rolls(UniformLootNumberProvider.create(1.0F, 1.0F))
                .with(this.applyExplosionDecay(block, primaryLootItem));

        // Crear un segundo LootPool y agregar el segundo LootItem
        LootPool.Builder secondaryLootPool = LootPool.builder()
                .rolls(UniformLootNumberProvider.create(1.0F, 1.0F))
                .with(this.applyExplosionDecay(block, secondaryLootItem));

        // Crear la tabla de loot con ambos pools
        return dropsWithSilkTouch(block, this.applyExplosionDecay(block,
                ItemEntry.builder(block)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))))
                .pool(primaryLootPool)
                .pool(secondaryLootPool);
    }

    /*
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
    */
}
