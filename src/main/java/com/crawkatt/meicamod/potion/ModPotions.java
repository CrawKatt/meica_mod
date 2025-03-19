package com.crawkatt.meicamod.potion;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, MeicaMod.MODID);

    public static final RegistryObject<Potion> BLESSING_FOREST_POTION = POTIONS.register("blessing_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.FOREST_BLESSING.get(), 9600, 0)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

}
