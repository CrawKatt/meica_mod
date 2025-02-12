package com.crawkatt.meicamod.entity.custom;

import com.crawkatt.meicamod.entity.goal.BlockAndCounterAttackGoal;
import com.crawkatt.meicamod.util.SyncSkinManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerCloneEntity extends PathAwareEntity {
    private Identifier playerSkin;

    public PlayerCloneEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setCustomNameVisible(true);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new BlockAndCounterAttackGoal(this));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer createPlayerCloneAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.00)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35D)
                .build();
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (this.getTarget() instanceof PlayerEntity player) {
            this.setCustomName(player.getName());
            UUID playerUUID = player.getUuid();
            String playerName = player.getName().getString();
            SyncSkinManager.get(playerName, playerUUID, skin -> this.playerSkin = skin);
        }
    }

    public Identifier getPlayerSkin() {
        ClientPlayerEntity skinLocation = MinecraftClient.getInstance().player;
        return skinLocation == null ? DefaultSkinHelper.getTexture() : this.playerSkin != null ? this.playerSkin : skinLocation.getSkinTexture();
    }

    public void setArmorInSlot(EquipmentSlot slot, ItemStack stack) {
        this.equipStack(slot, stack);
    }

    public void copyArmor(PlayerEntity player) {
        DefaultedList<ItemStack> armor = player.getInventory().armor;
        for (int i = 0; i < armor.size(); i++) {
            this.setArmorInSlot(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i), armor.get(i));
        }
    }

    @Override
    public void setStackInHand(@NotNull Hand hand, @NotNull ItemStack stack) {
        this.equipStack(hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, stack);
    }

    public void copyInventory(PlayerEntity player) {
        ItemStack strongestWeapon = ItemStack.EMPTY;
        ItemStack secondStrongestWeapon = ItemStack.EMPTY;

        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            ItemStack itemStack = player.getInventory().getStack(slot);
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
            this.setStackInHand(Hand.MAIN_HAND, strongestWeapon.copy());
        }

        // Verificar si el jugador lleva un escudo en la mano secundaria
        ItemStack offHandItem = player.getStackInHand(Hand.OFF_HAND);

        if (offHandItem.getItem() instanceof ShieldItem) {
            this.setStackInHand(Hand.OFF_HAND, offHandItem.copy());
        } else if (!secondStrongestWeapon.isEmpty()) {
            this.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        }
    }
}
