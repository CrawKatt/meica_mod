package com.crawkatt.meicamod.item;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.item.custom.CatalystItem;
import com.crawkatt.meicamod.item.custom.ModArmorItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    // Items básicos
    public static final Item BROTENITA_INGOT = registerItem("brotenita_ingot",
            new Item(new FabricItemSettings()));
    public static final Item RAW_BROTENITA = registerItem("raw_brotenita",
            new BlockItem(ModBlocks.RAW_BROTENITA, new FabricItemSettings()));
    public static final Item SMALL_BROTENITA = registerItem("small_brotenita",
            new AliasedBlockItem(ModBlocks.BROTENITA_CROP, new FabricItemSettings()));
    public static final Item BROTENITA_MEAL = registerItem("brotenita_meal",
            new Item(new FabricItemSettings()));

    // Armas y herramientas
    public static final Item BROTENITA_STAFF = registerItem("brotenita_staff",
            new CatalystItem());
    public static final Item MEICA_BOW = registerItem("meica_bow",
            new BowItem(new FabricItemSettings().maxDamage(500)));
    public static final Item BROTENITA_SWORD = registerItem("brotenita_sword",
            new SwordItem(ModToolMaterial.BROTENITA, 4, -2.4F, new FabricItemSettings()));
    public static final Item BROTENITA_PICKAXE = registerItem("brotenita_pickaxe",
            new PickaxeItem(ModToolMaterial.BROTENITA, 4, -2.8F, new FabricItemSettings()));
    public static final Item BROTENITA_AXE = registerItem("brotenita_axe",
            new AxeItem(ModToolMaterial.BROTENITA, 4.0F, -3.0F, new FabricItemSettings()));
    public static final Item BROTENITA_SHOVEL = registerItem("brotenita_shovel",
            new ShovelItem(ModToolMaterial.BROTENITA, 4.0F, -3.0F, new FabricItemSettings()));
    public static final Item BROTENITA_HOE = registerItem("brotenita_hoe",
            new HoeItem(ModToolMaterial.BROTENITA, 4, -3.0F, new FabricItemSettings()));

    // Armadura
    public static final Item BROTENITA_HELMET = registerItem("brotenita_helmet",
            new ModArmorItem(ModArmorMaterials.BROTENITA, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item BROTENITA_CHESTPLATE = registerItem("brotenita_chestplate",
            new ModArmorItem(ModArmorMaterials.BROTENITA, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item BROTENITA_LEGGINGS = registerItem("brotenita_leggings",
            new ModArmorItem(ModArmorMaterials.BROTENITA, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item BROTENITA_BOOTS = registerItem("brotenita_boots",
            new ModArmorItem(ModArmorMaterials.BROTENITA, ArmorItem.Type.BOOTS, new FabricItemSettings()));

    // Huevos de spawn
    public static final Item BROTECITO_SPAWN_EGG = registerItem("brotecito_spawn_egg",
            new SpawnEggItem(ModEntities.BROTECITO, 0x00FF00, 0xFFDAB9, new FabricItemSettings()));
    public static final Item MEICA_SPAWN_EGG = registerItem("meica_spawn_egg",
            new SpawnEggItem(ModEntities.MEICA, 0xA0522D, 0xFFDAB9, new FabricItemSettings()));
    public static final Item BROTECITO_MAMADO_SPAWN_EGG = registerItem("brotecito_mamado_spawn_egg",
            new SpawnEggItem(ModEntities.BROTECITO_MAMADO, 0xFFFF00, 0xFFD700, new FabricItemSettings()));
    public static final Item PLAYER_CLONE_SPAWN_EGG = registerItem("player_clone_spawn_egg",
            new SpawnEggItem(ModEntities.PLAYER_CLONE, 0x000000, 0xFFFFFF, new FabricItemSettings()));

    // Registro de items
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MeicaMod.MOD_ID, name), item);
    }

    // Añadir items al grupo de ingredientes
    private static void addItemsToIngredientGroup(FabricItemGroupEntries entries) {
        entries.add(BROTENITA_INGOT);
        entries.add(RAW_BROTENITA);
        entries.add(SMALL_BROTENITA);
        entries.add(BROTENITA_MEAL);
    }

    public static void registerModItems() {
        MeicaMod.LOGGER.info("Registering Mod Items for " + MeicaMod.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientGroup);
    }
}
