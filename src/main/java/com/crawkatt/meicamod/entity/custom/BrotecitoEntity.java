package com.crawkatt.meicamod.entity.custom;

import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.item.ModItems;
import com.crawkatt.meicamod.particle.ModParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.world.ServerWorld;
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

    public BrotecitoEntity(EntityType<? extends TameableEntity> pEntityType, World world) {
        super(pEntityType, world);
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
        this.goalSelector.add(7, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
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
        return ModEntities.BROTECITO.create(world);
    }

    // Método para que los Brotecitos puedan emitir partículas personalizadas al aparearse
    @Override
    public void handleStatus(byte id) {
        if (id == 18) {
            for(int i = 0; i < 7; i++) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                this.getWorld().addParticle(ModParticles.KAPPA_PRIDE_PARTICLES, this.getParticleX(1.0),this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), d0, d1, d2);
            }
        } else {
            super.handleStatus(id);
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

        Item itemForTaming = Items.APPLE;

        if (item == itemForTaming && !isTamed()) {
            return this.tame(itemStack, player, hand);
        }

        if (isTamed() && item instanceof SwordItem) {
            this.equipStack(EquipmentSlot.MAINHAND, itemStack);
            itemStack.decrement(1);
            return ActionResult.SUCCESS;
        }

        if (isTamed()) {
            if (item == ModItems.BROTENITA_MEAL) {
                return this.evolve(player, itemStack);
            } else if (!this.getWorld().isClient && hand == Hand.MAIN_HAND) {
                this.sitEntity(!this.isSitting());
                return ActionResult.SUCCESS;
            }
        }

        if (itemStack.getItem() == itemForTaming) {
            return ActionResult.PASS;
        }

        return super.interactMob(player, hand);
    }

    private ActionResult tame(ItemStack itemStack, PlayerEntity player, Hand hand) {
        if (isBreedingItem(itemStack)) {
            return super.interactMob(player, hand);
        }

        if (this.getWorld().isClient) {
            return ActionResult.CONSUME;
        } else {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            super.setOwner(player);
            this.navigation.recalculatePath();
            this.setTarget(null);
            this.getWorld().sendEntityStatus(this, (byte) 7);
            setSitting(false);

            return ActionResult.SUCCESS;
        }
    }

    // Método para evolucionar al Brotecito
    private ActionResult evolve(PlayerEntity player, ItemStack itemStack) {
        if (this.getWorld().isClient) {
            return ActionResult.CONSUME;
        } else {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

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
    }

    @Override
    public void writeCustomDataToNbt(@NotNull NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putBoolean("isSitting", this.isSitting());
    }

    // Método para que el Brotecito pueda sentarse y levantarse
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SITTING, false);
    }

    public void sitEntity(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
        this.setSitting(sitting);
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
        if (tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        } else if (!tAnimationState.isMoving() && !this.isInSittingPose()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito.idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        } else if (this.isInSittingPose()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito.idle", Animation.LoopType.HOLD_ON_LAST_FRAME));
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
