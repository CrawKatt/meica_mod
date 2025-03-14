package com.crawkatt.meicamod.potion;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.effect.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModPotions {
    public static final Potion BLESSING_FOREST_POTION = registerPotion("blessing_forest_potion",
            new Potion("blessing_potion", new StatusEffectInstance(ModEffects.FOREST_BLESSING, 6000, 0, true, false, true)));

    private static Potion registerPotion(String name, Potion potion) {
        return Registry.register(Registries.POTION, new Identifier(MeicaMod.MOD_ID, name), potion);
    }

    public static void registerPotions() {
        MeicaMod.LOGGER.info("Registering Potions for " + MeicaMod.MOD_ID);
    }
}
