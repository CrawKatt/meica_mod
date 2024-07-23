package com.example.examplemod.worldgen.tree.custom;

import com.example.examplemod.worldgen.tree.ModFoliagePlacers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class BigOakFoliagePlacer extends FoliagePlacer {
    public static final Codec<BigOakFoliagePlacer> CODEC = RecordCodecBuilder.create(bigOakFoliagePlacerInstance ->
            foliagePlacerParts(bigOakFoliagePlacerInstance).and(Codec.intRange(0, 16).fieldOf("height")
                    .forGetter(fp -> fp.height)).apply(bigOakFoliagePlacerInstance, BigOakFoliagePlacer::new));

    private final int height;

    public BigOakFoliagePlacer(IntProvider pRadius, IntProvider pOffset, int height) {
        super(pRadius, pOffset);
        this.height = height;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacers.BIG_OAK_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader pLevel, FoliageSetter pBlockSetter, RandomSource pRandom, TreeConfiguration pConfig,
                                 int pMaxFreeTreeHeight, FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {
        BlockPos blockpos = pAttachment.pos().above(pOffset);
        boolean flag = pAttachment.doubleTrunk();
        if (flag) {
            this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, blockpos, pOffset + 2, -1, flag);
            this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, blockpos, pOffset + 3, 0, flag);
            this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, blockpos, pOffset + 2, 1, flag);
            if (pRandom.nextBoolean()) {
                this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, blockpos, pOffset, 2, flag);
            }
        } else {
            this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, blockpos, pOffset + 2, -1, flag);
            this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, blockpos, pOffset + 1, 0, flag);
        }
    }

    @Override
    public int foliageHeight(RandomSource pRandom, int pHeight, TreeConfiguration pConfig) {
        return this.height;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource pRandom, int pLocalX, int pLocalY, int pLocalZ, int pRange, boolean pLarge) {
        if (pRange == -1 && !pLarge) {
            return pRange == pRange && pRange == pRange;
        } else if (pRange == 1) {
            return pRange + pRange > pRange * 2 - 2;
        } else {
            return false;
        }
    }
}
