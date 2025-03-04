package com.crawkatt.meicamod.item;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup BROTENITA_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(MeicaMod.MOD_ID, "brotenita"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.brotenita"))
                    .icon(() -> new ItemStack(ModBlocks.BROTENITA)).entries((displayContext, entries) -> {
                        entries.add(ModItems.SMALL_BROTENITA);
                        entries.add(ModBlocks.BROTENITA);
                        entries.add(ModBlocks.BROTENITA_BLOCK);
                        entries.add(ModBlocks.RAW_BROTENITA_BLOCK);
                        entries.add(ModBlocks.BROTENITA_STAIRS);
                        entries.add(ModBlocks.BROTENITA_BUTTON);
                        entries.add(ModBlocks.BROTENITA_PRESSURE_PLATE);
                        entries.add(ModBlocks.BROTENITA_FENCE);
                        entries.add(ModBlocks.BROTENITA_FENCE_GATE);
                        entries.add(ModBlocks.BROTENITA_WALL);
                        entries.add(ModBlocks.BROTENITA_DOOR);
                        entries.add(ModBlocks.BROTENITA_TRAPDOOR);
                        entries.add(ModBlocks.BROTENITA_SLAB);

                        entries.add(ModItems.BROTENITA_PICKAXE);
                        entries.add(ModItems.BROTENITA_AXE);
                        entries.add(ModItems.BROTENITA_SHOVEL);
                        entries.add(ModItems.BROTENITA_SWORD);
                        entries.add(ModItems.BROTENITA_HOE);
                        entries.add(ModItems.BROTENITA_STAFF);
                        entries.add(ModItems.BROTENITA_MEAL);
                        entries.add(ModItems.MEICA_BOW);

                        entries.add(ModItems.BROTENITA_HELMET);
                        entries.add(ModItems.BROTENITA_CHESTPLATE);
                        entries.add(ModItems.BROTENITA_LEGGINGS);
                        entries.add(ModItems.BROTENITA_BOOTS);

                        entries.add(ModBlocks.BROTENITA_MELTER);

                        entries.add(ModItems.MEICA_SPAWN_EGG);
                        entries.add(ModItems.BROTECITO_SPAWN_EGG);
                        entries.add(ModItems.BROTECITO_MAMADO_SPAWN_EGG);
                        entries.add(ModItems.PLAYER_CLONE_SPAWN_EGG);

                        entries.add(ModItems.BROTENITA_INGOT);

                    }).build());

    public static void registerItemGroups() {
        MeicaMod.LOGGER.info("Registering Item Groups for " + MeicaMod.MOD_ID);
    }
}
