package com.crawkatt.meicamod.screen;

import com.crawkatt.meicamod.entity.custom.BrotecitoEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class BrotecitoScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    public final BrotecitoEntity entity;

    private static final int ARROW_SLOT = 0;
    private static final int SHIELD_SLOT = 1;
    private static final int BOW_SLOT = 2;
    private static final int SWORD_SLOT = 3;

    public BrotecitoScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(27), (BrotecitoEntity) playerInventory.player.getWorld().getEntityById(buf.readInt()));
    }

    public BrotecitoScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, BrotecitoEntity entity) {
        super(ModScreenHandlers.BROTECITO_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.entity = entity;
        inventory.onOpen(playerInventory.player);

        this.addSlot(new Slot(inventory, ARROW_SLOT, 44, 26) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() == Items.ARROW;
            }
        });

        this.addSlot(new Slot(inventory, SHIELD_SLOT, 44, 44) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof ShieldItem;
            }

            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                updateEquipment();
            }
        });

        this.addSlot(new Slot(inventory, BOW_SLOT, 113, 26) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof BowItem;
            }

            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                updateEquipment();
            }
        });

        this.addSlot(new Slot(inventory, SWORD_SLOT, 113, 44) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof SwordItem;
            }

            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                updateEquipment();
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
        /*
        return !this.entity.areInventoriesDifferent(this.inventory)
                && this.inventory.canPlayerUse(player)
                && this.entity.isAlive()
                && this.entity.distanceTo(player) < 8.0F;
        */
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            int i = this.inventory.size();
            if (slot < i) {
                if (!this.insertItem(itemStack2, i, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).canInsert(itemStack2) && !this.getSlot(1).hasStack()) {
                if (!this.insertItem(itemStack2, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).canInsert(itemStack2)) {
                if (!this.insertItem(itemStack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i <= 2 || !this.insertItem(itemStack2, 2, i, false)) {
                int k = i + 27;
                int m = k + 9;
                if (slot >= k && slot < m) {
                    if (!this.insertItem(itemStack2, i, k, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slot < k) {
                    if (!this.insertItem(itemStack2, k, m, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(itemStack2, k, k, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
        }

        return itemStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }

    public void updateEquipment() {
        ItemStack swordStack = inventory.getStack(SWORD_SLOT);
        ItemStack bowStack = inventory.getStack(BOW_SLOT);
        ItemStack shieldStack = inventory.getStack(SHIELD_SLOT);

        if (!swordStack.isEmpty() && swordStack.getItem() instanceof SwordItem) {
            entity.equipStack(EquipmentSlot.MAINHAND, swordStack);
        } else if (!bowStack.isEmpty() && bowStack.getItem() instanceof BowItem) {
            entity.equipStack(EquipmentSlot.MAINHAND, bowStack);
        } else {
            entity.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }

        if (!shieldStack.isEmpty() && shieldStack.getItem() instanceof ShieldItem) {
            entity.equipStack(EquipmentSlot.OFFHAND, shieldStack);
        } else {
            entity.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}