package com.example.examplemod.item;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.ModEntityTypes;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MODID);

    public static final RegistryObject<Item> BROTECITO_SPAWN_EGG = ITEMS.register("brotecito_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.BROTECITO, 0xadd262, 0x111111,
                    new Item.Properties().tab(ModCreativeModeTab.EXAMPLE_TAB)));

    public static final RegistryObject<Item> MEICA_SPAWN_EGG = ITEMS.register("meica_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.MEICA, 0x175450, 0x111111,
                    new Item.Properties().tab(ModCreativeModeTab.MEICA_TAB)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
