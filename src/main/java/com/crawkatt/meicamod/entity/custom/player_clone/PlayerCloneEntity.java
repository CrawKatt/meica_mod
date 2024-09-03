package com.crawkatt.meicamod.entity.custom.player_clone;

import com.crawkatt.meicamod.util.SyncSkinManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerCloneEntity extends PathfinderMob {
    private ResourceLocation playerSkin;

    public PlayerCloneEntity(EntityType<? extends PathfinderMob> entityType, Level pLevel) {
        super(entityType, pLevel);
        this.setCustomNameVisible(true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new BlockAndCounterAttackGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.00)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .build();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.getTarget() instanceof Player player) {
            this.setCustomName(player.getName());
            UUID playerUUID = player.getUUID();
            String playerName = player.getName().getString();
            SyncSkinManager.get(playerName, playerUUID, skin -> this.playerSkin = skin);
        }
    }

    public ResourceLocation getPlayerSkin() {
        LocalPlayer skinLocation = Minecraft.getInstance().player;
        return skinLocation == null ? DefaultPlayerSkin.getDefaultSkin() : this.playerSkin != null ? this.playerSkin : skinLocation.getSkinTextureLocation();
    }

    public void setArmorInSlot(EquipmentSlot slot, ItemStack stack) {
        this.setItemSlot(slot, stack);
    }

    public void copyArmor(Player player) {
        NonNullList<ItemStack> armor = player.getInventory().armor;
        for (int i = 0; i < armor.size(); i++) {
            this.setArmorInSlot(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, i), armor.get(i));
        }
    }

    @Override
    public void setItemInHand(@NotNull InteractionHand hand, @NotNull ItemStack stack) {
        this.setItemSlot(hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, stack);
    }

    public void copyInventory(Player player) {
        ItemStack strongestWeapon = ItemStack.EMPTY;
        ItemStack secondStrongestWeapon = ItemStack.EMPTY;

        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack itemStack = player.getInventory().getItem(slot);
            if (itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof AxeItem) {
                if (strongestWeapon.isEmpty() || itemStack.getMaxDamage() > strongestWeapon.getMaxDamage()) {
                    secondStrongestWeapon = strongestWeapon;
                    strongestWeapon = itemStack;
                } else if (secondStrongestWeapon.isEmpty() || itemStack.getMaxDamage() > secondStrongestWeapon.getMaxDamage()) {
                    secondStrongestWeapon = itemStack;
                }
            }
        }

        if (!strongestWeapon.isEmpty()) {
            this.setItemInHand(InteractionHand.MAIN_HAND, strongestWeapon.copy());
        }

        // Verificar si el jugador lleva un escudo en la mano secundaria
        ItemStack offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);

        if (offHandItem.getItem() instanceof ShieldItem) {
            this.setItemInHand(InteractionHand.OFF_HAND, offHandItem.copy());
        } else if (!secondStrongestWeapon.isEmpty()) {
            this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }
    }
}
