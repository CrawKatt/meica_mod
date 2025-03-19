package com.crawkatt.meicamod.block.custom;

import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.brotecito.BrotecitoEntity;
import com.crawkatt.meicamod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BrotecitoSproutBlock extends CropBlock {
    public static final int MAX_AGE = 7;

    public BrotecitoSproutBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    @NotNull
    protected ItemLike getBaseSeedId() {
        return ModItems.BROTECITO_SEEDS.get();
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
        return false;
    }

    @Override
    @NotNull
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        int age = state.getValue(this.getAgeProperty());

        if (age < 7) {
            return super.getShape(state, world, pos, context);
        } else {
            return Block.box(2, 0, 2, 14, 5, 14);
        }
    }

    @Override
    public void playerWillDestroy(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (!level.isClientSide && isMaxAge(state)) {
            spawnBrotecito((ServerLevel) level, pos, (ServerPlayer) player);
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    private void spawnBrotecito(ServerLevel level, BlockPos pos, ServerPlayer player) {
        BrotecitoEntity brotecito = new BrotecitoEntity(ModEntities.BROTECITO.get(), level);
        brotecito.absMoveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);

        if (player != null) {
            brotecito.setTame(true);
            brotecito.setOwnerUUID(player.getUUID());
        }

        level.addFreshEntity(brotecito);
        level.removeBlock(pos, false);
    }
}
