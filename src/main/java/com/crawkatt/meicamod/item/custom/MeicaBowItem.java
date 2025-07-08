package com.crawkatt.meicamod.item.custom;

import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class MeicaBowItem extends BowItem {
    private static final String SPELL_KEY = "selected_spell";

    public MeicaBowItem(Settings settings) {
        super(settings);
    }

    public static int getSelectedSpell(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getInt(SPELL_KEY);
    }

    public static void setSelectedSpell(ItemStack stack, int spell) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(SPELL_KEY, spell);
    }
}
