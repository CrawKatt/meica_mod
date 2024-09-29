package com.crawkatt.meicamod.recipe;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MeicaMod.MODID);

    public static final RegistryObject<RecipeSerializer<BrotenitaMelterRecipe>> BROTENITA_MELTER_SERIALIZER =
            SERIALIZERS.register("brotenita_melter", () -> BrotenitaMelterRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
