package com.crawkatt.meicamod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class HollowLogBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape X_SHAPE = createHollowLogShape(Block.box(0.0, 3.0, 3.0, 16.0, 13.0, 13.0));
    public static final VoxelShape Y_SHAPE = createHollowLogShape(Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0));
    public static final VoxelShape Z_SHAPE = createHollowLogShape(Block.box(3.0, 3.0, 0.0, 13.0, 13.0, 16.0));

    /**
     * Crea la forma del tronco hueco restando un agujero de un cubo completo.
     * @param holeShape Cuboide que representa el hueco en el tronco.
     * @return VoxelShape del tronco hueco.
     */
    private static VoxelShape createHollowLogShape(VoxelShape holeShape) {
        return Shapes.join(Shapes.block(), holeShape, BooleanOp.ONLY_FIRST);
    }

    private final BlockState strippedState;

    /**
     * @param strippedState El tronco pelado que se genera al usar un hacha.
     * @param properties Configuración del bloque.
     */
    public HollowLogBlock(BlockState strippedState, BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(BlockStateProperties.AXIS, Direction.Axis.Y)
                .setValue(WATERLOGGED, false));
        this.strippedState = strippedState;
    }

    public BlockState getStrippedState() {
        return this.strippedState;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(AXIS, ctx.getClickedFace().getAxis())
                .setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    @NotNull
    public BlockState updateShape(
            BlockState pState,
            @NotNull Direction pDirection,
            @NotNull BlockState pNeighborState,
            @NotNull LevelAccessor pLevel,
            @NotNull BlockPos pPos,
            @NotNull BlockPos pNeighborPos
    ) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }

    @Override
    @NotNull
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    private VoxelShape getShape(BlockState state) {
        return switch (state.getValue(AXIS)) {
            case X -> X_SHAPE;
            case Y -> Y_SHAPE;
            case Z -> Z_SHAPE;
        };
    }

    @Override
    @NotNull
    public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getShape(state);
    }

    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getShape(state);
    }

    /**
     * Implementación manual del pelado con hacha para evitar que se pierda la propiedad WATERLOGGED.
     */
    @Override
    @NotNull
    public InteractionResult use(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos, Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit
    ) {
        ItemStack stack = player.getItemInHand(hand);
        if (getStrippedState() != null && stack.getItem() instanceof AxeItem) {
            level.setBlock(pos, getStrippedState().setValue(AXIS, state.getValue(AXIS)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)), 3);
            level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!player.isCreative()) {
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
