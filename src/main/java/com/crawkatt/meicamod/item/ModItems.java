package com.crawkatt.meicamod.item;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.entity.ModEntities;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item BROTECITO_SEEDS = registerItem("brotecito_seeds",
            new AliasedBlockItem(ModBlocks.BROTECITO_SPROUT, new FabricItemSettings()));

    public static final Item MEICA_BOW = registerItem("meica_bow",
            new BowItem(new FabricItemSettings().maxDamage(500)));

    public static final Item BROTECITO_SPAWN_EGG = registerItem("brotecito_spawn_egg",
            new SpawnEggItem(ModEntities.BROTECITO, 0x00FF00, 0xFFDAB9, new FabricItemSettings()));
    public static final Item MEICA_SPAWN_EGG = registerItem("meica_spawn_egg",
            new SpawnEggItem(ModEntities.MEICA, 0xA0522D, 0xFFDAB9, new FabricItemSettings()));
    public static final Item BROTECITO_MAMADO_SPAWN_EGG = registerItem("brotecito_mamado_spawn_egg",
            new SpawnEggItem(ModEntities.BROTECITO_MAMADO, 0xFFFF00, 0xFFD700, new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MeicaMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        MeicaMod.LOGGER.info("Registering Mod Items for " + MeicaMod.MOD_ID);
    }
}
