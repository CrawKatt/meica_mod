package com.crawkatt.meicamod.item.custom;

import com.crawkatt.meicamod.effect.ModEffects;
import com.google.common.collect.ImmutableMap;
import com.crawkatt.meicamod.item.ModArmorMaterials;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ModArmorItem extends ArmorItem {
    private static final Map<ArmorMaterial, MobEffectInstance> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, MobEffectInstance>())
                    .put(ModArmorMaterials.BROTENITA, new MobEffectInstance(ModEffects.FOREST_BLESSING.get(), -1, 0,
                            false, false, true)).build();

    private static final Map<Player, Map<ArmorMaterial, MobEffectInstance>> PLAYER_EFFECTS = new HashMap<>();

    public ModArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack pStack, Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide()) {
            if (pEntity instanceof Player player) {
                evaluateArmorEffects(player);
            }
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    private void evaluateArmorEffects(Player player) {
        for (Map.Entry<ArmorMaterial, MobEffectInstance> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            MobEffectInstance mapStatusEffect = entry.getValue();

            if (hasCorrectArmorOn(mapArmorMaterial, player)) {
                addStatusEffectForMaterial(player, mapArmorMaterial, mapStatusEffect);
            } else {
                removeStatusEffectIfPresent(player, mapStatusEffect);
            }
        }
    }

    private void removeStatusEffectIfPresent(Player player, MobEffectInstance mapStatusEffect) {
        if (player.hasEffect(mapStatusEffect.getEffect())) {
            MobEffectInstance currentEffect = player.getEffect(mapStatusEffect.getEffect());
            if (currentEffect != null && effectsMatch(currentEffect, mapStatusEffect)) {
                player.removeEffect(mapStatusEffect.getEffect());
            }
        }
    }

    private boolean effectsMatch(MobEffectInstance a, MobEffectInstance b) {
        return a.getEffect() == b.getEffect() &&
                a.getAmplifier() == b.getAmplifier() &&
                a.isAmbient() == b.isAmbient() &&
                a.isVisible() == b.isVisible() &&
                a.showIcon() == b.showIcon();
    }

    private void addStatusEffectForMaterial(Player player, ArmorMaterial mapArmorMaterial,
                                            MobEffectInstance mapStatusEffect) {
        boolean hasPlayerEffect = player.hasEffect(mapStatusEffect.getEffect());

        if (hasCorrectArmorOn(mapArmorMaterial, player) && !hasPlayerEffect) {
            player.addEffect(new MobEffectInstance(mapStatusEffect));
            PLAYER_EFFECTS.computeIfAbsent(player, k -> new HashMap<>()).put(material, mapStatusEffect);
        }
    }

    private boolean hasCorrectArmorOn(ArmorMaterial material, Player player) {
        for (ItemStack armorStack : player.getInventory().armor) {
            if (!(armorStack.getItem() instanceof ArmorItem)) {
                return false;
            }
        }

        ArmorItem boots = ((ArmorItem) player.getInventory().getArmor(0).getItem());
        ArmorItem leggings = ((ArmorItem) player.getInventory().getArmor(1).getItem());
        ArmorItem breastplate = ((ArmorItem) player.getInventory().getArmor(2).getItem());
        ArmorItem helmet = ((ArmorItem) player.getInventory().getArmor(3).getItem());

        return helmet.getMaterial() == material && breastplate.getMaterial() == material
                && leggings.getMaterial() == material && boots.getMaterial() == material;
    }
}
