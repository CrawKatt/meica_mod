package com.crawkatt.meicamod.block.entity;

import com.crawkatt.meicamod.block.custom.BrotenitaMelterBlock;
import com.crawkatt.meicamod.networking.ModMessages;
import com.crawkatt.meicamod.recipe.BrotenitaMelterRecipe;
import com.crawkatt.meicamod.screen.BrotenitaMelterScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;

public class BrotenitaMelterBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int FLUID_INPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;
    private static final int IRON_ITEM_SLOT = 3;

    public static int getInputSlot() { return INPUT_SLOT; }
    public static int getFluidInputSlot() { return FLUID_INPUT_SLOT; }
    public static int getOutputSlot() { return OUTPUT_SLOT; }
    public static int getIronItemSlot() { return IRON_ITEM_SLOT; }

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 200;

    public BrotenitaMelterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BROTENITA_MELTER_BE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BrotenitaMelterBlockEntity.this.progress;
                    case 1 -> BrotenitaMelterBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BrotenitaMelterBlockEntity.this.progress = value;
                    case 1 -> BrotenitaMelterBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    public ItemStack getRenderStack() {
        if (this.getStack(OUTPUT_SLOT).isEmpty()) {
            return this.getStack(INPUT_SLOT);
        } else {
            return this.getStack(OUTPUT_SLOT);
        }
    }

    @Override
    public void markDirty() {
        if (!world.isClient()) {
            PacketByteBuf data = PacketByteBufs.create();
            data.writeInt(inventory.size());
            for (int i = 0; i < inventory.size(); i++) {
                data.writeItemStack(inventory.get(i));
            }
            data.writeBlockPos(getPos());

            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                ServerPlayNetworking.send(player, ModMessages.ITEM_SYNC, data);
            }
        }
        super.markDirty();
    }

    public void setInventory(DefaultedList<ItemStack> list) {
        for (int i = 0; i < inventory.size(); i++) {
            this.inventory.set(i, inventory.get(i));
        }
    }

    public final SimpleEnergyStorage ironStorage = new SimpleEnergyStorage(64000, 200, 200) {
        @Override
        protected void onFinalCommit() {
            markDirty();
            getWorld().updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    };

    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return (FluidConstants.BUCKET / 81) * 64; // 1 Bucket = 81000 Droplets = 1000mB || *64 ==> 64,000mb
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
            getWorld().updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    };

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        Direction localDir = this.getWorld().getBlockState(pos).get(BrotenitaMelterBlock.FACING);

        if (side == Direction.DOWN) {
            return false;
        }

        if (side == Direction.UP) {
            return slot == INPUT_SLOT;
        }

        return switch (localDir) {
            default -> // NORTH
                    side.getOpposite() == Direction.NORTH && slot == INPUT_SLOT ||
                    side.getOpposite() == Direction.WEST && slot == INPUT_SLOT;

            case EAST ->
                    side.rotateYClockwise() == Direction.NORTH && slot == INPUT_SLOT ||
                    side.rotateYClockwise() == Direction.WEST && slot == INPUT_SLOT;

            case SOUTH ->
                    side == Direction.NORTH && slot == INPUT_SLOT ||
                    side == Direction.WEST && slot == INPUT_SLOT;

            case WEST ->
                    side.rotateYCounterclockwise() == Direction.NORTH && slot == INPUT_SLOT ||
                    side.rotateYCounterclockwise() == Direction.WEST && slot == INPUT_SLOT;
        };
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        Direction localDir = this.getWorld().getBlockState(this.pos).get(BrotenitaMelterBlock.FACING);

        if (side == Direction.UP) {
            return false;
        }

        if (side == Direction.DOWN) {
            return slot == OUTPUT_SLOT;
        }

        return switch (localDir) {
            default -> side.getOpposite() == Direction.SOUTH && slot == OUTPUT_SLOT ||
                    side.getOpposite() == Direction.EAST && slot == OUTPUT_SLOT;

            case EAST -> side.rotateYClockwise() == Direction.SOUTH && slot == OUTPUT_SLOT ||
                    side.rotateYClockwise() == Direction.EAST && slot == OUTPUT_SLOT;

            case SOUTH -> side == Direction.SOUTH && slot == OUTPUT_SLOT ||
                    side == Direction.EAST && slot == OUTPUT_SLOT;

            case WEST -> side.rotateYCounterclockwise() == Direction.SOUTH && slot == OUTPUT_SLOT ||
                    side.rotateYCounterclockwise() == Direction.EAST && slot == OUTPUT_SLOT;
        };
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Brotenita Melter");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BrotenitaMelterScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("brotenita_melter_station.progress", progress);
        nbt.putLong(("brotenita_melter.iron"), ironStorage.amount);
        nbt.put("brotenita_melter.variant", fluidStorage.variant.toNbt());
        nbt.putLong("brotenita_melter.fluid_amount", fluidStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("brotenita_melter_station.progress");
        ironStorage.amount = nbt.getLong("brotenita_melter.iron");
        fluidStorage.variant = FluidVariant.fromNbt((NbtCompound) nbt.get("brotenita_melter.variant"));
        fluidStorage.amount = nbt.getLong("brotenita_melter.fluid_amount");
        super.readNbt(nbt);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        fillUpOnIron();
        fillUpOnFluid();

        if (canInsertOutputSlot() && hasRecipe()) {
            increaseCraftingProgress();
            extractIron();
            markDirty(world, pos, state);

            if (hasCraftingFinished()) {
                craftItem();
                extractFluid();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void extractFluid() {
        try(Transaction transaction = Transaction.openOuter()) {
            this.fluidStorage.extract(FluidVariant.of(Fluids.LAVA), 500, transaction);
            transaction.commit();
        }
    }

    private void fillUpOnFluid() {
        if (hasFluidSourceItemInFluidSlot(FLUID_INPUT_SLOT)) {
            transferItemFluidToTank(FLUID_INPUT_SLOT);
        }
    }

    private void transferItemFluidToTank(int fluidItemSlot) {
        try(Transaction transaction = Transaction.openOuter()) {
            this.fluidStorage.insert(FluidVariant.of(Fluids.LAVA),
                    (FluidConstants.BUCKET / 81), transaction);
            transaction.commit();

            this.setStack(fluidItemSlot, new ItemStack(Items.BUCKET));
        }
    }

    private boolean hasFluidSourceItemInFluidSlot(int fluidItemSlot) {
        return this.getStack(fluidItemSlot).getItem() == Items.LAVA_BUCKET;
    }

    private void extractIron() {
        try(Transaction transaction = Transaction.openOuter()) {
            this.ironStorage.extract(32L, transaction);
            transaction.commit();
        }
    }

    private void fillUpOnIron() {
        if (hasIronItemInIronSlot(IRON_ITEM_SLOT)) {
            try (Transaction transaction = Transaction.openOuter()) {
                long REQUIRED_FLUID = 500;
                long MAX_IRON_AMOUNT = 64;

                FluidVariant lavaVariant = FluidVariant.of(Fluids.LAVA);
                long extracted = this.fluidStorage.extract(lavaVariant, REQUIRED_FLUID, transaction);

                if (extracted < REQUIRED_FLUID) {
                    transaction.abort();
                    return;
                }

                long inserted = this.ironStorage.insert(MAX_IRON_AMOUNT, transaction);

                if (inserted < MAX_IRON_AMOUNT) {
                    transaction.abort();
                    return;
                }

                transaction.commit();

                ItemStack stack = getStack(IRON_ITEM_SLOT);
                stack.decrement(1);
                setStack(IRON_ITEM_SLOT, stack);
                markDirty();
            }
        }
    }

    private boolean hasIronItemInIronSlot(int ironItemSlot) {
        return this.getStack(ironItemSlot).getItem() == Items.IRON_BLOCK;
    }

    private void craftItem() {
        Optional<BrotenitaMelterRecipe> recipe = getCurrentRecipe();

        this.removeStack(INPUT_SLOT, 1);

        this.setStack(OUTPUT_SLOT, new ItemStack(recipe.get().getOutput(null).getItem(),
                this.getStack(OUTPUT_SLOT).getCount() + recipe.get().getOutput(null).getCount()));
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    private boolean hasRecipe() {
        Optional<BrotenitaMelterRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().getOutput(null);

        return canInsertAmountIntoOutputSlot(output.getCount())
                && canInsertIntoOutputSlot(output) && hasEnoughtIronToCraft() && hasEnoughtFluidToCraft();
    }

    private boolean hasEnoughtFluidToCraft() {
        return this.fluidStorage.amount >= 500; // mB amount!
    }

    private boolean hasEnoughtIronToCraft() {
        return this.ironStorage.amount >= 32L * this.maxProgress;
    }

    private boolean canInsertIntoOutputSlot(ItemStack output) {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.getStack(OUTPUT_SLOT).getMaxCount() >= getStack(OUTPUT_SLOT).getCount() + count;
    }

    private Optional<BrotenitaMelterRecipe> getCurrentRecipe() {
        SimpleInventory inventory = new SimpleInventory((this.size()));
        for (int i = 0; i < this.size(); i++) {
            inventory.setStack(i, this.getStack(i));
        }

        return this.getWorld().getRecipeManager().getFirstMatch(BrotenitaMelterRecipe.Type.INSTANCE, inventory, this.getWorld());
    }

    private boolean canInsertOutputSlot() {
        return this.getStack(OUTPUT_SLOT).isEmpty() ||
                this.getStack(OUTPUT_SLOT).getCount() < this.getStack(OUTPUT_SLOT).getMaxCount();
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
