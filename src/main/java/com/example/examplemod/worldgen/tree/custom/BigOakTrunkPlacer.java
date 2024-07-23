package com.example.examplemod.worldgen.tree.custom;

import com.example.examplemod.worldgen.tree.ModTrunkPlacerTypes;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class BigOakTrunkPlacer extends TrunkPlacer {
    public static final Codec<BigOakTrunkPlacer> CODEC = RecordCodecBuilder.create(bigOakTrunkPlacerInstace ->
            trunkPlacerParts(bigOakTrunkPlacerInstace).apply(bigOakTrunkPlacerInstace, BigOakTrunkPlacer::new));

    public BigOakTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.BIG_OAK_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter,
                                                            RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
        BlockPos blockpos = pPos.below();
        setDirtAt(pLevel, pBlockSetter, pRandom, blockpos, pConfig);
        setDirtAt(pLevel, pBlockSetter, pRandom, blockpos.east(), pConfig);
        setDirtAt(pLevel, pBlockSetter, pRandom, blockpos.south(), pConfig);
        setDirtAt(pLevel, pBlockSetter, pRandom, blockpos.south().east(), pConfig);
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(pRandom);
        int i = pFreeTreeHeight - pRandom.nextInt(4);
        int j = 2 - pRandom.nextInt(3);
        int k = pPos.getX();
        int l = pPos.getY();
        int i1 = pPos.getZ();
        int j1 = k;
        int k1 = i1;
        int l1 = l + pFreeTreeHeight - 1;

        for(int i2 = 0; i2 < pFreeTreeHeight; ++i2) {
            if (i2 >= i && j > 0) {
                j1 += direction.getStepX();
                k1 += direction.getStepZ();
                --j;
            }

            int j2 = l + i2;
            BlockPos blockpos1 = new BlockPos(j1, j2, k1);
            if (TreeFeature.isAirOrLeaves(pLevel, blockpos1)) {
                this.placeLog(pLevel, pBlockSetter, pRandom, blockpos1, pConfig);
                this.placeLog(pLevel, pBlockSetter, pRandom, blockpos1.east(), pConfig);
                this.placeLog(pLevel, pBlockSetter, pRandom, blockpos1.south(), pConfig);
                this.placeLog(pLevel, pBlockSetter, pRandom, blockpos1.east().south(), pConfig);
            }
        }

        list.add(new FoliagePlacer.FoliageAttachment(new BlockPos(j1, l1, k1), 0, true));

        for(int l2 = -1; l2 <= 2; ++l2) {
            for(int i3 = -1; i3 <= 2; ++i3) {
                if ((l2 < 0 || l2 > 1 || i3 < 0 || i3 > 1) && pRandom.nextInt(3) <= 0) {
                    int j3 = pRandom.nextInt(3) + 2;

                    for(int k2 = 0; k2 < j3; ++k2) {
                        this.placeLog(pLevel, pBlockSetter, pRandom, new BlockPos(k + l2, l1 - k2 - 1, i1 + i3), pConfig);
                    }

                    list.add(new FoliagePlacer.FoliageAttachment(new BlockPos(j1 + l2, l1, k1 + i3), 0, false));
                }
            }
        }

        // Variar la longitud de los brazos y la altura de inicio
        int minBranchLength = 2;
        int maxBranchLength = 5;
        int branchVariationHeight = pFreeTreeHeight / 4;

        for (int height = branchVariationHeight; height < pFreeTreeHeight; height += pRandom.nextInt(3) + 2) {
            int branchLength = pRandom.nextInt(maxBranchLength - minBranchLength + 1) + minBranchLength;
            BlockPos branchBasePos = new BlockPos(pPos.getX(), pPos.getY() + height, pPos.getZ());

            for (Direction direction1 : Direction.Plane.HORIZONTAL) {
                BlockPos branchPos = branchBasePos;
                int upwardShift = 0;
                for (int length = 0; length < branchLength; length++) {
                    // Alternar entre moverse hacia arriba y en la dirección horizontal para crear un efecto de escalera
                    if (length % 2 == 0 && upwardShift < 2) {
                        branchPos = branchPos.above();
                        upwardShift++;
                    } else {
                        branchPos = branchPos.relative(direction1);
                    }
                    if (TreeFeature.isAirOrLeaves(pLevel, branchPos) || TreeFeature.isAirOrLeaves(pLevel, branchPos.below())) {
                        this.placeLog(pLevel, pBlockSetter, pRandom, branchPos, pConfig);
                    }
                }
                // Aumentar la densidad de follaje alrededor de los extremos de los brazos
                addFoliageAround(pLevel, pBlockSetter, pRandom, branchPos, pConfig, true, 6);
            }
        }

        return list;
    }

    private void addFoliageAround(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pos, TreeConfiguration pConfig, boolean isBranchEnd, int extraDensity) {
        // Base foliage density plus extra density for branch ends
        int foliageDensity = isBranchEnd ? (4 + extraDensity) : 2;
        for (int i = 0; i < foliageDensity; i++) {
            // Randomly choose a direction to place foliage, including diagonals for more natural spread
            Direction direction = Direction.values()[pRandom.nextInt(Direction.values().length)];
            // Randomly decide how far from the branch the foliage should start (1 or 2 blocks away)
            int distance = pRandom.nextBoolean() ? 1 : 2;
            BlockPos foliagePos = pos.relative(direction, distance);
            // Place foliage if the spot is suitable (air or leaves)
            if (TreeFeature.isAirOrLeaves(pLevel, foliagePos)) {
                this.placeLeaf(pLevel, pBlockSetter, pRandom, foliagePos, pConfig);
            }
            // Optionally, add more foliage around the initial foliage position for a denser look
            if (isBranchEnd && pRandom.nextFloat() < 0.5f) { // 50% chance to add more foliage around for branch ends
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    BlockPos aroundFoliagePos = foliagePos.relative(dir);
                    if (TreeFeature.isAirOrLeaves(pLevel, aroundFoliagePos)) {
                        this.placeLeaf(pLevel, pBlockSetter, pRandom, aroundFoliagePos, pConfig);
                    }
                }
            }
        }
    }

    private void placeLeaf(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, BlockPos pos, TreeConfiguration config) {
        blockSetter.accept(pos, config.foliageProvider.getState(random, pos));
    }
}
