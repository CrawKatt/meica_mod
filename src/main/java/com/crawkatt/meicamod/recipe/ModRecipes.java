package com.crawkatt.meicamod.recipe;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(MeicaMod.MOD_ID, BrotenitaMelterRecipe.Serializer.ID),
                BrotenitaMelterRecipe.Serializer.INSTANCE);

        Registry.register(Registries.RECIPE_TYPE, new Identifier(MeicaMod.MOD_ID, BrotenitaMelterRecipe.Serializer.ID),
                BrotenitaMelterRecipe.Type.INSTANCE);
    }
}
