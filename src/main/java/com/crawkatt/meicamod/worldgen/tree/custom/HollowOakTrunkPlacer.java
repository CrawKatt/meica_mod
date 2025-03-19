package com.crawkatt.meicamod.worldgen.tree.custom;

import com.crawkatt.meicamod.worldgen.tree.ModTrunkPlacerTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class HollowOakTrunkPlacer extends TrunkPlacer {
    public static final Codec<HollowOakTrunkPlacer> CODEC = RecordCodecBuilder.create(hollowOakTrunkPlacerInstance ->
            fillTrunkPlacerFields(hollowOakTrunkPlacerInstance).apply(hollowOakTrunkPlacerInstance, HollowOakTrunkPlacer::new));

    public HollowOakTrunkPlacer(int baseHeight, int firstRandomHeight, int seconrdRandomHeight) {
        super(baseHeight, firstRandomHeight, seconrdRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return ModTrunkPlacerTypes.HOLLOW_OAK_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random,
                                                 int height, BlockPos startPos, TreeFeatureConfig config) {
        List<FoliagePlacer.TreeNode> nodes = new ArrayList<>();

        Direction.Axis axis = random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
        BlockPos.Mutable pos = new BlockPos.Mutable(startPos.getX(), startPos.getY(), startPos.getZ());

        for (int i = 0; i < height; i++) {
            BlockState trunkState = config.trunkProvider.get(random, pos).with(Properties.AXIS, axis);
            replacer.accept(pos, trunkState);

            if (axis == Direction.Axis.X) {
                pos.move(Direction.EAST);
            } else {
                pos.move(Direction.SOUTH);
            }
        }

        nodes.add(new FoliagePlacer.TreeNode(pos, 0, false));

        return nodes;
    }
}
