package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    public ModAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement rootAdvancement = Advancement.Builder.create()
                .display(new AdvancementDisplay(new ItemStack(ModBlocks.BROTENITA.asItem()),
                        Text.literal("Un mineral extraño y peligroso"), Text.literal("Extrae un mineral de Brotenita"),
                        new Identifier(MeicaMod.MOD_ID, "textures/block/brotenita.png"), AdvancementFrame.TASK,
                        true, true, false))
                .criterion("has_brotenita", InventoryChangedCriterion.Conditions.items(ModBlocks.BROTENITA.asItem()))
                .build(consumer, MeicaMod.MOD_ID + ":meicamod");

        Advancement meicaIsDefeatedAdvancement = Advancement.Builder.create()
                .parent(rootAdvancement)
                .display(new AdvancementDisplay(new ItemStack(ModItems.BROTENITA_SWORD),
                        Text.literal("Una planta muy esquizo"), Text.literal("Derrota a Meica"),
                        new Identifier(MeicaMod.MOD_ID, "textures/block/brotenita.png"), AdvancementFrame.TASK,
                        true, true, false))
                .criterion("has_meica_is_defeated", OnKilledCriterion.Conditions.createPlayerKilledEntity(EntityPredicate.Builder.create().type(ModEntities.MEICA)))
                .build(consumer, MeicaMod.MOD_ID + ":meica_is_defeated");
    }
}
