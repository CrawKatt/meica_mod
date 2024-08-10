package com.crawkatt.meicamod.item;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.item.custom.ModArmorItem;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MeicaMod.MODID);

    public static final RegistryObject<Item> BROTENITA_INGOT = ITEMS.register("brotenita_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_BROTENITA = ITEMS.register("raw_brotenita",
            () -> new BlockItem(ModBlocks.RAW_BROTENITA.get(), new Item.Properties()));

    public static final RegistryObject<Item> BROTENITA_MEAL = ITEMS.register("brotenita_meal",
            () -> new Item(new Item.Properties()));

    // todo: Cambiar texturas por texturas de Brotenita
    public static final RegistryObject<Item> BROTENITA_STAFF = ITEMS.register("brotenita_staff",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> MEICA_BOW = ITEMS.register("meica_bow",
            () -> new BowItem(new Item.Properties().durability(500)));

    public static final RegistryObject<Item> BROTENITA_SWORD = ITEMS.register("brotenita_sword",
            () -> new SwordItem(ModToolTiers.BROTENITA, 4, 2, new Item.Properties()));

    public static final RegistryObject<Item> BROTENITA_PICKAXE = ITEMS.register("brotenita_pickaxe",
            () -> new PickaxeItem(ModToolTiers.BROTENITA, 4, 2, new Item.Properties()));

    public static final RegistryObject<Item> BROTENITA_AXE = ITEMS.register("brotenita_axe",
            () -> new AxeItem(ModToolTiers.BROTENITA, 4, 2, new Item.Properties()));

    public static final RegistryObject<Item> BROTENITA_SHOVEL = ITEMS.register("brotenita_shovel",
            () -> new ShovelItem(ModToolTiers.BROTENITA, 4, 2, new Item.Properties()));

    public static final RegistryObject<Item> BROTENITA_HOE = ITEMS.register("brotenita_hoe",
            () -> new HoeItem(ModToolTiers.BROTENITA, 4, 2, new Item.Properties()));

    public static final RegistryObject<Item> BROTENITA_HELMET = ITEMS.register("brotenita_helmet",
            () -> new ModArmorItem(ModArmorMaterials.BROTENITA, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> BROTENITA_CHESTPLATE = ITEMS.register("brotenita_chestplate",
            () -> new ModArmorItem(ModArmorMaterials.BROTENITA, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<Item> BROTENITA_LEGGINGS = ITEMS.register("brotenita_leggings",
            () -> new ModArmorItem(ModArmorMaterials.BROTENITA, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<Item> BROTENITA_BOOTS = ITEMS.register("brotenita_boots",
            () -> new ModArmorItem(ModArmorMaterials.BROTENITA, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> BROTECITO_SPAWN_EGG = ITEMS.register("brotecito_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BROTECITO, 0x00FF00, 0xFFDAB9, new Item.Properties()));

    public static final RegistryObject<Item> MEICA_SPAWN_EGG = ITEMS.register("meica_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MEICA, 0xA0522D, 0xFFDAB9, new Item.Properties()));

    public static final RegistryObject<Item> BROTECITO_MAMADO_SPAWN_EGG = ITEMS.register("brotecito_mamado_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BROTECITO_MAMADO, 0xFFFF00, 0xFFD700, new Item.Properties()));

    public static final RegistryObject<Item> PLAYER_CLONE_SPAWN_EGG = ITEMS.register("player_clone_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PLAYER_CLONE, 0x000000, 0xFFFFFF, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
