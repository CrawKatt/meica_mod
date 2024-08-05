package com.crawkatt.meicamod.item;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    public static final Tier BROTENITA = TierSortingRegistry.registerTier(
            new ForgeTier(5, 1500, 5f, 4f, 25,
                    ModTags.Blocks.NEEDS_BROTENITA_TOOL, () -> Ingredient.of(ModItems.BROTENITA_INGOT.get())),
            new ResourceLocation(MeicaMod.MODID, "brotenita"), List.of(Tiers.NETHERITE), List.of());
}
