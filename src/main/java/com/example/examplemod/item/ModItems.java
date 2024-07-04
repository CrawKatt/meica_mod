package com.example.examplemod.item;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MODID);

    public static final RegistryObject<Item> BROTENITA_INGOT = ITEMS.register("brotenita_ingot",
            () -> new Item(new Item.Properties()));

    // todo: Cambiar textura por textura de Brotenita cruda
    public static final RegistryObject<Item> RAW_BROTENITA = ITEMS.register("raw_brotenita",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BROTENITA_STAFF = ITEMS.register("brotenita_staff",
            () -> new Item(new Item.Properties().stacksTo(1)));

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

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
