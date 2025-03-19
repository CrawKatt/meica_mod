package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.entity.ModEntities;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModAdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.@NotNull Provider registries, @NotNull Consumer<Advancement> saver, @NotNull ExistingFileHelper existingFileHelper) {
        Advancement rootAdvancement = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModBlocks.RAW_BROTENITA.get()),
                        Component.literal("Un mineral extraño y peligroso"), Component.literal("Extrae un mineral de Brotenita"),
                        new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "textures/block/oak_log.png"), FrameType.TASK,
                        true, true, false))
                .addCriterion("has_brotenita", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.RAW_BROTENITA.get()))
                .save(saver, new ResourceLocation(MeicaMod.MODID, "meicamod"), existingFileHelper);

        Advancement meicaIsDefeatedAdvancement = Advancement.Builder.advancement()
                .parent(rootAdvancement)
                .display(new DisplayInfo(new ItemStack(ModBlocks.RAW_BROTENITA.get()),
                        Component.literal("Una planta muy esquizo"), Component.literal("Derrota a Meica"),
                        null, FrameType.TASK,
                        true, true, false))
                .addCriterion("has_meica_is_defeated", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ModEntities.MEICA.get()))))
                .save(saver, new ResourceLocation(MeicaMod.MODID, "meica_is_defeated"), existingFileHelper);

    }
}
