package com.crawkatt.meicamod.entity.custom;

import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.goal.BrotecitoMateGoal;
import com.crawkatt.meicamod.item.ModItems;
import com.crawkatt.meicamod.particle.ModParticles;
import com.crawkatt.meicamod.screen.BrotecitoScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class BrotecitoEntity extends TameableEntity implements Angerable, GeoEntity {
    @Nullable
    private UUID persistentAngerTarget;
    private static final UniformIntProvider PERSISTENT_ANGER_TIME = UniformIntProvider.create(20, 39);
    private int evolutionProgress = 0;
    private static final int MAX_EVOLUTION_PROGRESS = 5;
    private static final TrackedData<Integer> DATA_REMAINING_ANGER_TIME = DataTracker.registerData(BrotecitoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(BrotecitoEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean aggressiveMode = false;
    private final SimpleInventory inventory = new SimpleInventory(27);

    public BrotecitoEntity(EntityType<? extends TameableEntity> pEntityType, World world) {
        super(pEntityType, world);
        this.setTamed(false);
    }

    public NamedScreenHandlerFactory createScreenHandlerFactory() {
        return new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeInt(BrotecitoEntity.this.getId());
            }

            @Override
            public Text getDisplayName() {
                return Text.translatable("screen.meicamod.brotecito");
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                if (!isOwner(player)) {
                    return null;
                }

                return new BrotecitoScreenHandler(syncId, playerInventory, inventory, BrotecitoEntity.this);
            }
        };
    }

    public int getEvolutionProgress() {
        return this.evolutionProgress;
    }

    public void increaseEvolutionProgress(int amount) {
        this.evolutionProgress += amount;
        if (this.evolutionProgress > MAX_EVOLUTION_PROGRESS) {
            this.evolutionProgress = MAX_EVOLUTION_PROGRESS;
        }
    }

    public int getMaxEvolutionProgress() {
        return MAX_EVOLUTION_PROGRESS;
    }

    // Método para que el Brotecito pueda obtener espadas de diamante y equiparlas
    @Override
    public void tick() {
        super.tick();

        /* Añadir flechas en caso de que el Brotecito sea arquero
        if (!this.hasItemInSlot(EquipmentSlot.OFFHAND)) {
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.ARROW, 1));
        }
        */
    }

    /**
     * Define el comportamiento de la IA del Brotecito
     */
    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(4, new PounceAtTargetGoal(this, 0.4F));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
        this.goalSelector.add(7, new BrotecitoMateGoal(this, 1.0));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F) {
            @Override
            public boolean canStart() {
                return !BrotecitoEntity.this.isSitting() && super.canStart();
            }
        });
        this.goalSelector.add(10, new LookAroundGoal(this) {
            @Override
            public boolean canStart() {
                return !BrotecitoEntity.this.isSitting() && super.canStart();
            }
        });
        if (this.isAgressiveMode()) {
            this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        } else {
            this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
            this.targetSelector.add(2, new AttackWithOwnerGoal(this));
            this.targetSelector.add(3, new RevengeGoal(this).setGroupRevenge());
            this.targetSelector.add(6, new UntamedActiveTargetGoal<>(this, TurtleEntity.class, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
            this.targetSelector.add(7, new ActiveTargetGoal<>(this, AbstractSkeletonEntity.class, false));
            this.targetSelector.add(8, new UniversalAngerGoal<>(this, true));
        }
    }

    public static DefaultAttributeContainer createBrotecitoAttributes() {
        return AnimalEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.1f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2f)
                .build();
    }

    public void setAggressiveMode(boolean aggressiveMode) {
        this.aggressiveMode = aggressiveMode;
    }

    public boolean isAgressiveMode() {
        return this.aggressiveMode;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(@NotNull ServerWorld world, @NotNull PassiveEntity pOtherParent) {
        return null;
    }

    // Método para que los Brotecitos puedan emitir partículas personalizadas al aparearse
    @Override
    public void handleStatus(byte status) {
        if (status == 18) {
            spawnBreedingParticles();
        } else if (status == 19 || status == 20) {
            spawnSitParticles();
        } else if (status == 44) {
            spawnEvolveParticles();
        } else {
            super.handleStatus(status);
        }
    }

    private void spawnEvolveParticles() {
        for (int i = 0; i < 7; ++i) {
            this.getWorld().addParticle(
                    ParticleTypes.SMOKE,
                    this.getParticleX(1.0),
                    this.getRandomBodyY() + 0.5,
                    this.getParticleZ(1.0),
                    (this.random.nextDouble() - 0.5) * 0.1,
                    0.0,
                    (this.random.nextDouble() - 0.5) * 0.1
            );
        }
    }

    private void spawnBreedingParticles() {
        for(int i = 0; i < 7; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.getWorld().addParticle(ModParticles.KAPPA_PRIDE_PARTICLES, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), d, e, f);
        }
    }

    public void spawnSitParticles() {
        BlockState dirtState = Blocks.DIRT.getDefaultState();
        ParticleEffect particleOptions = new BlockStateParticleEffect(ParticleTypes.BLOCK, dirtState);
        for (int i = 0; i < 15; ++i) {
            double offsetX = (this.random.nextDouble() - 0.5) * 0.5;
            double offsetY = 0.3;
            double offsetZ = (this.random.nextDouble() - 0.5) * 0.5;

            this.getWorld().addParticle(
                    particleOptions,
                    this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ,
                    0.0, 0.1, 0.0
            );
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack pStack) {
        return pStack.isOf(Items.CARROT);
    }

    @Override
    public int getAngerTime() {
        return this.dataTracker.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setAngerTime(int i) {
        this.dataTracker.set(DATA_REMAINING_ANGER_TIME, i);
    }

    @Nullable
    @Override
    public UUID getAngryAt() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setAngryAt(@Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(PERSISTENT_ANGER_TIME.get(this.random));
    }

    // Método para que el Brotecito pueda ser domado con manzanas y evolucionar con Polvo de Brotenita
    @Override
    public @NotNull ActionResult interactMob(PlayerEntity player, @NotNull Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        if (this.getWorld().isClient) {
            boolean canInteract = this.isOwner(player) || this.isTamed() || (itemStack.isOf(Items.APPLE) && !this.isTamed() && !this.hasAngerTime());
            return canInteract ? ActionResult.CONSUME : ActionResult.PASS;
        }

        if (isBreedingItem(itemStack)) {
            return super.interactMob(player, hand);
        }

        if (itemStack.isOf(Items.APPLE) && !this.isTamed() && !this.hasAngerTime()) {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            if (this.random.nextInt(3) == 0) {
                this.setOwner(player);
                this.navigation.stop();
                this.setTarget(null);
                this.setSitting(true);
                this.getWorld().sendEntityStatus(this, (byte)7);
            } else {
                this.getWorld().sendEntityStatus(this, (byte)6);
            }

            return ActionResult.SUCCESS;
        }

        // Resto de interacciones cuando está domesticado
        if (isTamed()) {
            if (player.isSneaking() && hand == Hand.MAIN_HAND) {
                player.openHandledScreen(this.createScreenHandlerFactory());
                return ActionResult.CONSUME;
            }

            if (item == ModItems.BROTENITA_MEAL) {
                return this.evolve(player, itemStack);
            } else if (!this.getWorld().isClient && hand == Hand.MAIN_HAND) {
                this.sitEntity(!this.isSitting());
                return ActionResult.SUCCESS;
            }
        }

        if (itemStack.isOf(Items.APPLE)) {
            return ActionResult.PASS;
        }

        return super.interactMob(player, hand);
    }

    // Método para evolucionar al Brotecito
    private ActionResult evolve(PlayerEntity player, ItemStack itemStack) {
        if (this.getWorld().isClient) {
            return ActionResult.CONSUME;
        } else {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            this.getWorld().sendEntityStatus(this, (byte)44);
            this.increaseEvolutionProgress(1);
            if (this.getEvolutionProgress() >= this.getMaxEvolutionProgress()) {
                World world = this.getWorld();
                BrotecitoMamadoEntity brotecitoMamado = new BrotecitoMamadoEntity(ModEntities.BROTECITO_MAMADO, world);

                brotecitoMamado.updatePosition(this.getX(), this.getY(), this.getZ());
                brotecitoMamado.setTamed(true);
                brotecitoMamado.setOwner(player);

                world.spawnEntity(brotecitoMamado);
                this.discard();

                return ActionResult.SUCCESS;
            }

            return ActionResult.SUCCESS;
        }
    }

    // Método para que el Brotecito pueda atacar a entidades hostiles excepto a:
    // - Ghasts
    // - Brotecitos que no son suyos
    // - jugadores que no pueden ser dañados
    @Override
    public boolean canAttackWithOwner(@NotNull LivingEntity pTarget, @NotNull LivingEntity pOwner) {
        if (!(pTarget instanceof GhastEntity)) {
            if (pTarget instanceof BrotecitoEntity brotecitoEntity) {
                return !brotecitoEntity.isTamed() || brotecitoEntity.getOwner() != pOwner;
            } else if (pTarget instanceof PlayerEntity && pOwner instanceof PlayerEntity && !((PlayerEntity)pOwner).shouldDamagePlayer((PlayerEntity)pTarget)) {
                return false;
            } else if (pTarget instanceof AbstractHorseEntity && ((AbstractHorseEntity)pTarget).isTame()) {
                return false;
            } else {
                return !(pTarget instanceof TameableEntity) || !((TameableEntity)pTarget).isTamed();
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean isTeammate(@NotNull Entity pEntity) {
        if (isAttacking() || !this.isTamed() && pEntity instanceof MeicaEntity) {
            return true;
        }

        if (this.isTamed() && this.getOwner() != null) {
            return pEntity == this.getOwner();
        } else {
            return super.isTeammate(pEntity);
        }
    }

    @Override
    public void readCustomDataFromNbt(@NotNull NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        setSitting(tag.getBoolean("isSitting"));
        this.readAngerFromNbt(this.getWorld(), tag);

        NbtList list = tag.getList("Inventory", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound stackTag = list.getCompound(i);
            int slot = stackTag.getByte("Slot") & 255;
            if (slot < inventory.size()) {
                inventory.setStack(slot, ItemStack.fromNbt(stackTag));
            }
        }
    }

    @Override
    public void writeCustomDataToNbt(@NotNull NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putBoolean("isSitting", this.isSitting());
        this.writeAngerToNbt(tag);

        NbtList list = new NbtList();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                NbtCompound stackTag = new NbtCompound();
                stackTag.putByte("Slot", (byte) i);
                stack.writeNbt(stackTag);
                list.add(stackTag);
            }
        }

        tag.put("Inventory", list);
    }

    // Método para que el Brotecito pueda sentarse y levantarse
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DATA_REMAINING_ANGER_TIME, 0);
        this.dataTracker.startTracking(SITTING, false);
    }

    public void sitEntity(boolean sitting) {
        if (this.isSitting() != sitting) {
            this.dataTracker.set(SITTING, sitting);
            this.setSitting(sitting);
            this.playSound(sitting ? SoundEvents.BLOCK_GRAVEL_BREAK : SoundEvents.BLOCK_GRASS_BREAK, 1.0F, 0.8F);
            this.getWorld().sendEntityStatus(this, sitting ? (byte) 19 : (byte) 20);
        }
    }

    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    @Override
    public AbstractTeam getScoreboardTeam() {
        return super.getScoreboardTeam();
    }

    @Override
    public boolean canBeLeashedBy(@NotNull PlayerEntity player) {
        return false;
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(60.0D);
            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(4D);
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5f);
        } else {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(30.0D);
            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(2D);
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25f);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (this.isSitting()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito.sit", Animation.LoopType.HOLD_ON_LAST_FRAME));
            return PlayState.CONTINUE;
        }

        if (tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (!tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito.idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public boolean isEntityTamed() {
        return this.isTamed();
    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }
}
