package com.crawkatt.meicamod.block.custom;

import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.BrotecitoEntity;
import com.crawkatt.meicamod.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class BrotecitoSproutBlock extends CropBlock {
    public static final int MAX_AGE = 7;

    public BrotecitoSproutBlock(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return ModItems.BROTECITO_SEEDS;
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        int age = state.get(this.getAgeProperty());

        if (age < 7) {
            return super.getOutlineShape(state, world, pos, context);
        } else {
            return Block.createCuboidShape(2, 0, 2, 14, 5, 14);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && isMature(state)) {
            spawnBrotecito((ServerWorld) world, pos, (ServerPlayerEntity) player);
        }
        super.onBreak(world, pos, state, player);
    }

    private void spawnBrotecito(ServerWorld world, BlockPos pos, ServerPlayerEntity player) {
        BrotecitoEntity brotecito = new BrotecitoEntity(ModEntities.BROTECITO, world);
        brotecito.updatePosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);

        if (player != null) {
            brotecito.setTamed(true);
            brotecito.setOwner(player);
        }

        world.spawnEntity(brotecito);
        world.removeBlock(pos, false);
    }
}
