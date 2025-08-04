package com.crawkatt.meicamod.block.custom;

import com.crawkatt.meicamod.block.entity.MeicaStatueBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class MeicaStatueBlock extends BlockWithEntity {
    public MeicaStatueBlock(Settings settings) {
        super(settings);
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MeicaStatueBlockEntity(pos, state);
    }
}
