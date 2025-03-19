package com.crawkatt.meicamod.worldgen.tree.custom;

import com.crawkatt.meicamod.worldgen.tree.ModTrunkPlacerTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class HollowOakTrunkPlacer extends TrunkPlacer {
    public static final Codec<HollowOakTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            trunkPlacerParts(instance).apply(instance, HollowOakTrunkPlacer::new));

    public HollowOakTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected @NotNull TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.HOLLOW_OAK_TRUNK_PLACER.get();
    }

    @Override
    @NotNull
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(
            @NotNull LevelSimulatedReader level,
            @NotNull BiConsumer<BlockPos, BlockState> blockSetter,
            @NotNull RandomSource random, int treeHeight,
            @NotNull BlockPos startPos,
            @NotNull TreeConfiguration config
    ) {
        List<FoliagePlacer.FoliageAttachment> foliageNodes = new ArrayList<>();

        Direction.Axis axis = random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(startPos.getX(), startPos.getY(), startPos.getZ());

        for (int i = 0; i < treeHeight; i++) {
            BlockState trunkState = config.trunkProvider.getState(random, startPos)
                    .setValue(RotatedPillarBlock.AXIS, axis);

            blockSetter.accept(mutablePos, trunkState);

            if (axis == Direction.Axis.X) {
                mutablePos.move(Direction.EAST);
            } else {
                mutablePos.move(Direction.SOUTH);
            }
        }

        foliageNodes.add(new FoliagePlacer.FoliageAttachment(mutablePos.immutable(), 0, false));

        return foliageNodes;
    }
}
