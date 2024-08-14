package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiConsumer;

// Clase ModEntityLootTables
// Se encarga de generar las tablas de saqueo de las entidades (hace que droppeen items al morir)
public class ModEntityLootTables implements LootTableSubProvider {
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(
                new ResourceLocation(MeicaMod.MODID, "entities/meica"),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(ForgeRegistries.ITEMS.getValue(new ResourceLocation(MeicaMod.MODID, "meica_bow"))))
                        )
        );
    }
}