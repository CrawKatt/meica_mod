package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.BROTENITA_HELMET,
                        ModItems.BROTENITA_CHESTPLATE,
                        ModItems.BROTENITA_LEGGINGS,
                        ModItems.BROTENITA_BOOTS)
                .add(ModItems.BROTENITA_PICKAXE,
                     ModItems.BROTENITA_AXE,
                     ModItems.BROTENITA_SHOVEL,
                     ModItems.BROTENITA_HOE,
                     ModItems.BROTENITA_SWORD);

        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.HOLLOW_OAK_LOG.asItem(),
                     ModBlocks.STRIPPED_HOLLOW_OAK_LOG.asItem());
    }
}
