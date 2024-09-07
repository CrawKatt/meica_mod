package com.crawkatt.meicamod.block.portal;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.util.ModTags;
import com.crawkatt.meicamod.worldgen.dimension.ModDimensions;
import com.crawkatt.meicamod.worldgen.portal.MeicaTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class MeicaPortalBlock extends NetherPortalBlock {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_AABB = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
    protected static final VoxelShape Z_AABB = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

    public MeicaPortalBlock() {
        super(Properties.of()
                .strength(-1F)
                .noCollission()
                .lightLevel(state -> 10)
                .noLootTable()
                .mapColor(MapColor.COLOR_LIGHT_GREEN)
        );
        registerDefaultState(stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(AXIS) == Direction.Axis.Z ? Z_AABB : X_AABB;
    }

    public boolean trySpawnPortal(LevelAccessor worldIn, BlockPos pos) {
        MeicaPortalBlock.Size MeicaPortalBlock$size = this.isPortal(worldIn, pos);
        if (MeicaPortalBlock$size != null && !onTrySpawnPortal(worldIn, pos, MeicaPortalBlock$size)) {
            playPortalSound((Level) worldIn, pos);
            MeicaPortalBlock$size.placePortalBlocks();
            return true;
        } else {
            return false;
        }
    }

    private void playPortalSound(Level worldIn, BlockPos pos) {
        if (worldIn != null) {
            worldIn.playSound(null, pos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    public static boolean onTrySpawnPortal(LevelAccessor world, BlockPos pos, MeicaPortalBlock.Size size) {
        return MinecraftForge.EVENT_BUS.post(new BlockEvent.PortalSpawnEvent(world, pos, world.getBlockState(pos), size));
    }

    @Nullable
    public MeicaPortalBlock.Size isPortal(LevelAccessor worldIn, BlockPos pos) {
        MeicaPortalBlock.Size KJPortalBlock$size = new Size(worldIn, pos, Direction.Axis.X);
        if (KJPortalBlock$size.isValid() && KJPortalBlock$size.portalBlockCount == 0) {
            return KJPortalBlock$size;
        } else {
            MeicaPortalBlock.Size MeicaPortalBlock$size1 = new Size(worldIn, pos, Direction.Axis.Z);
            return MeicaPortalBlock$size1.isValid() && MeicaPortalBlock$size1.portalBlockCount == 0 ? MeicaPortalBlock$size1 : null;
        }
    }

    @Override
    public @NotNull BlockState updateShape(BlockState stateIn, Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor worldIn, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        Direction.Axis direction$axis = facing.getAxis();
        Direction.Axis direction$axis1 = stateIn.getValue(AXIS);
        boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
        return !flag && facingState.getBlock() != this && !(new Size(worldIn, currentPos, direction$axis1)).validatePortal() ?
                Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, Entity entity) {
        if(!entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions()) {
            if(entity.isOnPortalCooldown()) {
                entity.setPortalCooldown();
            } else {
                if(!entity.level().isClientSide && !pos.equals(entity.portalEntrancePos)) {
                    entity.portalEntrancePos = pos.immutable();
                }
                Level entityWorld = entity.level();

                MinecraftServer minecraftserver = entityWorld.getServer();
                ResourceKey<Level> destination = entity.level().dimension() == ModDimensions.MEICADIM_LEVEL_KEY
                        ? Level.OVERWORLD : ModDimensions.MEICADIM_LEVEL_KEY;
                if(minecraftserver != null) {
                    ServerLevel destinationWorld = minecraftserver.getLevel(destination);
                    if(destinationWorld != null && minecraftserver.isNetherEnabled() && !entity.isPassenger()) {
                        entity.level().getProfiler().push("meica_portal");
                        entity.setPortalCooldown();
                        entity.changeDimension(destinationWorld, new MeicaTeleporter(destinationWorld));
                        entity.level().getProfiler().pop();
                    }
                }
            }
        }
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, RandomSource rand) {
        if (rand.nextInt(100) == 0) {
            worldIn.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D,
                    pos.getZ() + 0.5D, SoundEvents.PORTAL_AMBIENT,
                    SoundSource.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
        }

        for(int i = 0; i < 4; ++i) {
            double x = pos.getX() + rand.nextDouble();
            double y = pos.getY() + rand.nextDouble();
            double z = pos.getZ() + rand.nextDouble();
            double xSpeed = (rand.nextFloat() - 0.5D) * 0.5D;
            double ySpeed = (rand.nextFloat() - 0.5D) * 0.5D;
            double zSpeed = (rand.nextFloat() - 0.5D) * 0.5D;
            int j = rand.nextInt(2) * 2 - 1;
            if (!worldIn.getBlockState(pos.west()).is(this) && !worldIn.getBlockState(pos.east()).is(this)) {
                x = pos.getX() + 0.5D + 0.25D * j;
                xSpeed = rand.nextFloat() * 2.0F * j;
            } else {
                z = pos.getZ() + 0.5D + 0.25D * j;
                zSpeed = rand.nextFloat() * 2.0F * j;
            }

            worldIn.addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, Rotation rot) {
        return switch (rot) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.getValue(AXIS)) {
                case Z -> state.setValue(AXIS, Direction.Axis.X);
                case X -> state.setValue(AXIS, Direction.Axis.Z);
                default -> state;
            };
            default -> state;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    public static class Size extends PortalShape {
        private final LevelAccessor level;
        private final Direction.Axis axis;
        private final Direction rightDir;
        private final Direction leftDir;
        private int portalBlockCount;
        @Nullable
        private BlockPos bottomLeft;
        private int height;
        private int width;

        public Size(LevelAccessor level, BlockPos pos, Direction.Axis axis) {
            super(level, pos, axis);
            this.level = level;
            this.axis = axis;
            if (axis == Direction.Axis.X) {
                this.leftDir = Direction.EAST;
                this.rightDir = Direction.WEST;
            } else {
                this.leftDir = Direction.NORTH;
                this.rightDir = Direction.SOUTH;
            }

            int i = this.getDistanceUntilEdge(pos, this.leftDir) - 1;
            if (i >= 0) {
                this.bottomLeft = pos.relative(this.leftDir, i);
                this.width = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);
                if (this.width < 2 || this.width > 21) {
                    this.bottomLeft = null;
                    this.width = 0;
                }
            }

            if (this.bottomLeft != null) {
                this.height = this.calculatePortalHeight();
            }

        }

        protected int getDistanceUntilEdge(BlockPos pos, Direction directionIn) {
            int i;
            for(i = 0; i < 22; ++i) {
                BlockPos blockpos = pos.relative(directionIn, i);
                if(this.canConnect(this.level.getBlockState(blockpos)) ||
                        !(this.level.getBlockState(blockpos.below()).is(ModTags.Blocks.PORTAL_FRAME_BLOCKS))) {
                    break;
                }
            }

            BlockPos framePos = pos.relative(directionIn, i);
            return this.level.getBlockState(framePos).is(ModTags.Blocks.PORTAL_FRAME_BLOCKS) ? i : 0;
        }

        protected int calculatePortalHeight() {
            label56:
            for(this.height = 0; this.height < 21; ++this.height) {
                for(int i = 0; i < this.width; ++i) {
                    if (this.bottomLeft != null) {
                        BlockPos blockpos = this.bottomLeft.relative(this.rightDir, i).above(this.height);
                        BlockState blockstate = this.level.getBlockState(blockpos);
                        if (this.canConnect(blockstate)) {
                            break label56;
                        }

                        Block block = blockstate.getBlock();
                        if (block == ModBlocks.MEICA_PORTAL.get()) {
                            ++this.portalBlockCount;
                        }

                        if (i == 0) {
                            BlockPos framePos = blockpos.relative(this.leftDir);
                            if (!(this.level.getBlockState(framePos).is(ModTags.Blocks.PORTAL_FRAME_BLOCKS))) {
                                break label56;
                            }
                        } else if (i == this.width - 1) {
                            BlockPos framePos = blockpos.relative(this.rightDir);
                            if (!(this.level.getBlockState(framePos).is(ModTags.Blocks.PORTAL_FRAME_BLOCKS))) {
                                break label56;
                            }
                        }
                    }
                }
            }

            for(int j = 0; j < this.width; ++j) {
                if (this.bottomLeft != null) {
                    BlockPos framePos = this.bottomLeft.relative(this.rightDir, j).above(this.height);
                    if (!(this.level.getBlockState(framePos).is(ModTags.Blocks.PORTAL_FRAME_BLOCKS))) {
                        this.height = 0;
                        break;
                    }
                }
            }

            if (this.height <= 21 && this.height >= 3) {
                return this.height;
            } else {
                this.bottomLeft = null;
                this.width = 0;
                this.height = 0;
                return 0;
            }
        }

        protected boolean canConnect(BlockState pos) {
            Block block = pos.getBlock();
            return !pos.isAir() && block != ModBlocks.MEICA_PORTAL.get();
        }

        public boolean isValid() {
            return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
        }

        public void placePortalBlocks() {
            for(int i = 0; i < this.width; ++i) {
                if (this.bottomLeft != null) {
                    BlockPos blockpos = this.bottomLeft.relative(this.rightDir, i);

                    for(int j = 0; j < this.height; ++j) {
                        this.level.setBlock(blockpos.above(j), ModBlocks.MEICA_PORTAL.get().defaultBlockState().setValue(MeicaPortalBlock.AXIS, this.axis), 18);
                    }
                }
            }
        }

        private boolean isPortalCountValidForSize() {
            return this.portalBlockCount >= this.width * this.height;
        }

        public boolean validatePortal() {
            return this.isValid() && this.isPortalCountValidForSize();
        }
    }
}
