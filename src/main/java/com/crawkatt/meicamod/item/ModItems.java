package com.crawkatt.meicamod.item;

import com.crawkatt.meicamod.MeicaMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item BROTENITA = registerItem("brotenita", new Item(new FabricItemSettings()));
    public static final Item SMALL_BROTENITA = registerItem("small_brotenita", new Item(new FabricItemSettings()));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(BROTENITA);
        entries.add(SMALL_BROTENITA);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MeicaMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        MeicaMod.LOGGER.info("Registering Mod Items for " + MeicaMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
