package com.crawkatt.meicamod.datagen.loot;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.block.custom.BrotenitaCropBlock;
import com.crawkatt.meicamod.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

// Clase ModBlockLootTables
// Se encarga de generar las tablas de loot de los bloques (los ítems que se obtienen al romperlos)
public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.BROTENITA_BLOCK.get());
        this.dropSelf(ModBlocks.BROTENITA_STAIRS.get());
        this.dropSelf(ModBlocks.BROTENITA_BUTTON.get());
        this.dropSelf(ModBlocks.BROTENITA_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.BROTENITA_TRAPDOOR.get());
        this.dropSelf(ModBlocks.BROTENITA_FENCE.get());
        this.dropSelf(ModBlocks.BROTENITA_FENCE_GATE.get());
        this.dropSelf(ModBlocks.BROTENITA_WALL.get());

        this.add(ModBlocks.BROTENITA_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.BROTENITA_SLAB.get()));
        this.add(ModBlocks.BROTENITA_DOOR.get(),
                block -> createDoorTable(ModBlocks.BROTENITA_DOOR.get()));
        this.add(ModBlocks.RAW_BROTENITA.get(),
                block -> createCopperLikeOreDrops(ModBlocks.RAW_BROTENITA.get(), ModItems.RAW_BROTENITA.get()));
        this.add(ModBlocks.RAW_BROTENITA_BLOCK.get(),
                block -> createCopperLikeOreDrops(ModBlocks.RAW_BROTENITA.get(), ModItems.RAW_BROTENITA.get()));

        // LootTables del Cultivo de Brotenita
        LootItemCondition.Builder lootItemConditionBuilder = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BROTENITA_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BrotenitaCropBlock.AGE, 6));

        this.add(ModBlocks.BROTENITA_CROP.get(), this.createCropDrops(ModBlocks.BROTENITA_CROP.get(),
                ModItems.RAW_BROTENITA.get(), ModItems.SMALL_BROTENITA.get(), lootItemConditionBuilder));
    }

    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
