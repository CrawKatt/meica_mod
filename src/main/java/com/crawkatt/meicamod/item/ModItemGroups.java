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
                    .icon(() -> new ItemStack(ModItems.BROTECITO_SEEDS)).entries((displayContext, entries) -> {
                        entries.add(ModItems.BROTECITO_SEEDS);

                        entries.add(ModItems.MEICA_BOW);
                        entries.add(ModItems.MEICA_SPAWN_EGG);
                        entries.add(ModItems.BROTECITO_SPAWN_EGG);
                        entries.add(ModItems.BROTECITO_MAMADO_SPAWN_EGG);

                        entries.add(ModBlocks.HOLLOW_OAK_LOG);
                        entries.add(ModBlocks.STRIPPED_HOLLOW_OAK_LOG);

                    }).build());

    public static void registerItemGroups() {
        MeicaMod.LOGGER.info("Registering Item Groups for " + MeicaMod.MOD_ID);
    }
}
