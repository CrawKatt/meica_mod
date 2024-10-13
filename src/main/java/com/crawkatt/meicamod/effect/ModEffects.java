package com.crawkatt.meicamod.effect;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
        DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MeicaMod.MODID);

    public static final RegistryObject<MobEffect> BROTIFICATION = MOB_EFFECTS.register("brotenita_infection",
        () -> new BrotenitaInfectionEffect(MobEffectCategory.HARMFUL, 0x996600).addAttributeModifier(Attributes.MOVEMENT_SPEED,
                "7107DE5E-7CE8-4030-940E-514C1F160890", -0.025f, AttributeModifier.Operation.fromValue(0)));

    public static final RegistryObject<MobEffect> PARANOIA = MOB_EFFECTS.register("paranoia",
            () -> new ParanoiaEffect(MobEffectCategory.HARMFUL, 0x9966CC));

    public static final RegistryObject<MobEffect> CAMOUFLAGE_COOLDOWN = MOB_EFFECTS.register("camouflage_cooldown",
            () -> new CamouflageCooldown(MobEffectCategory.HARMFUL, 0x9966CC));

    public static void register(IEventBus pEventBus) {
        MOB_EFFECTS.register(pEventBus);
    }
}
