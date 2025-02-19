package com.crawkatt.meicamod.effect;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects {

    public static final StatusEffect BROTIFICATION = registerStatusEffect("brotenita_infection",
            new BrotenitaInfectionEffect(StatusEffectCategory.HARMFUL, 0x996600).addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    "7107DE5E-7CE8-4030-940E-514C1F160890", -0.025f, EntityAttributeModifier.Operation.fromId(0)));

    public static final StatusEffect CAMOUFLAGE_COOLDOWN = registerStatusEffect("camouflage_cooldown",
            new CamouflageCooldown(StatusEffectCategory.HARMFUL, 0x9966CC));

    public static final StatusEffect PARANOIA = registerStatusEffect("paranoia",
            new ParanoiaEffect(StatusEffectCategory.HARMFUL, 0x996600));

    private static StatusEffect registerStatusEffect(String name, StatusEffect statusEffect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(MeicaMod.MOD_ID, name), statusEffect);
    }

    public static void registerEffects() {
        MeicaMod.LOGGER.info("Registering Mod Effects for " + MeicaMod.MOD_ID);
    }
}
