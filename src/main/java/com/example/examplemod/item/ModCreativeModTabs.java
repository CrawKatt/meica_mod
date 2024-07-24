package com.example.examplemod.item;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExampleMod.MODID);

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BROTENITA_INGOT.get()))
                    .title(Component.translatable("creativetab.example_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.BROTENITA_INGOT.get());
                        pOutput.accept(ModItems.RAW_BROTENITA.get());
                        pOutput.accept(ModItems.BROTENITA_MEAL.get());

                        pOutput.accept(ModItems.BROTENITA_STAFF.get());
                        pOutput.accept(ModItems.MEICA_BOW.get());

                        pOutput.accept(ModItems.BROTENITA_SWORD.get());
                        pOutput.accept(ModItems.BROTENITA_PICKAXE.get());
                        pOutput.accept(ModItems.BROTENITA_AXE.get());
                        pOutput.accept(ModItems.BROTENITA_SHOVEL.get());
                        pOutput.accept(ModItems.BROTENITA_HOE.get());

                        pOutput.accept(ModItems.BROTENITA_HELMET.get());
                        pOutput.accept(ModItems.BROTENITA_CHESTPLATE.get());
                        pOutput.accept(ModItems.BROTENITA_LEGGINGS.get());
                        pOutput.accept(ModItems.BROTENITA_BOOTS.get());

                        //pOutput.accept(ModBlocks.BROTENITA_BLOCK.get());
                        pOutput.accept(ModBlocks.BROTENITA_BLOCK.get());
                        pOutput.accept(ModBlocks.RAW_BROTENITA_BLOCK.get());

                        pOutput.accept(ModBlocks.BROTENITA_STAIRS.get());
                        pOutput.accept(ModBlocks.BROTENITA_SLAB.get());
                        pOutput.accept(ModBlocks.BROTENITA_BUTTON.get());
                        pOutput.accept(ModBlocks.BROTENITA_PRESSURE_PLATE.get());

                        pOutput.accept(ModBlocks.BROTENITA_FENCE.get());
                        pOutput.accept(ModBlocks.BROTENITA_FENCE_GATE.get());
                        pOutput.accept(ModBlocks.BROTENITA_WALL.get());

                        pOutput.accept(ModBlocks.BROTENITA_DOOR.get());
                        pOutput.accept(ModBlocks.BROTENITA_TRAPDOOR.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
