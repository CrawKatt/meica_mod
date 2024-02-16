package com.example.examplemod.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab EXAMPLE_TAB = new CreativeModeTab("exampletab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.BROTECITO_SPAWN_EGG.get());
        }
    };

    public static final CreativeModeTab MEICA_TAB = new CreativeModeTab("meicatab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MEICA_SPAWN_EGG.get());
        }
    };
}
