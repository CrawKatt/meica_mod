package com.crawkatt.meicamod.entity.custom;

import com.crawkatt.meicamod.entity.goal.BrotecitoMamadoMeleeAttackGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

public class BrotecitoMamadoEntity extends TameableEntity implements Angerable, GeoEntity {
    @Nullable
    private UUID persistentAngerTarget;
    private static final UniformIntProvider PERSISTENT_ANGER_TIME = UniformIntProvider.create(20, 39);
    private static final TrackedData<Integer> DATA_REMAINING_ANGER_TIME = DataTracker.registerData(BrotecitoMamadoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(BrotecitoMamadoEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int attackAnimationTick;
    private static final int ATTACK_DURATION = 20;
    private static final TrackedData<Boolean> ATTACKING =
            DataTracker.registerData(BrotecitoMamadoEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public BrotecitoMamadoEntity(EntityType<? extends TameableEntity> pEntityType, World pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
            if (this.attackAnimationTick == 0) {
                stopAttack();
            }
        }
    }

    /**
     * Define el comportamiento de la IA del Brotecito
     * Es necesario añadir un goal personalizado para que la animación de ataque funcione correctamente
     */
    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(5, new BrotecitoMamadoMeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, (new RevengeGoal(this)).setGroupRevenge());
        this.targetSelector.add(6, new UntamedActiveTargetGoal<>(this, TurtleEntity.class, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
        this.targetSelector.add(7, new ActiveTargetGoal<>(this, AbstractSkeletonEntity.class, false));
        this.targetSelector.add(8, new UniversalAngerGoal<>(this, true));
    }

    /* Define los attributos del Brotecito Mamado */
    public static DefaultAttributeContainer createBrotecitoMamadoAttributes() {
        return AnimalEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 100.D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.1f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 5f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15f)
                .build();
    }

    @Nullable
    @Override
    public PassiveEntity createChild(@NotNull ServerWorld pLevel, @NotNull PassiveEntity pOtherParent) {
        return null;
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

    public void startAttack() {
        this.dataTracker.set(ATTACKING, true);
        this.attackAnimationTick = ATTACK_DURATION;
    }

    public void stopAttack() {
        this.dataTracker.set(ATTACKING, false);
        this.attackAnimationTick = 0;
    }

    @Override
    public @NotNull ActionResult interactMob(@NotNull PlayerEntity player, @NotNull Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        if (isTamed() && !this.getWorld().isClient && hand == Hand.MAIN_HAND) {
            sitEntity(!isSitting());
            return ActionResult.SUCCESS;
        }

        if (item == Items.CARROT) {
            this.heal((float)item.getFoodComponent().getHunger());
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
    }

    // Método para que el Brotecito pueda atacar a entidades hostiles excepto a:
    // - Ghasts
    // - Brotecitos que no son suyos
    // - jugadores que no pueden ser dañados
    @Override
    public boolean canAttackWithOwner(@NotNull LivingEntity pTarget, @NotNull LivingEntity pOwner) {
        return !isNonAttackableTarget(pTarget, pOwner);
    }

    private boolean isNonAttackableTarget(LivingEntity pTarget, LivingEntity pOwner) {
        if (pTarget instanceof GhastEntity) {
            return true;
        }

        if (pTarget instanceof BrotecitoEntity brotecitoEntity) {
            return brotecitoEntity.isTamed() && brotecitoEntity.getOwner() == pOwner;
        }

        if (pTarget instanceof BrotecitoMamadoEntity brotecitoMamadoEntity) {
            return brotecitoMamadoEntity.isTamed() && brotecitoMamadoEntity.getOwner() == pOwner;
        }

        if (pTarget instanceof PlayerEntity && pOwner instanceof PlayerEntity) {
            return !((PlayerEntity) pOwner).shouldDamagePlayer((PlayerEntity) pTarget);
        }

        if (pTarget instanceof AbstractHorseEntity) {
            return ((AbstractHorseEntity) pTarget).isTame();
        }

        return pTarget instanceof TameableEntity && ((TameableEntity) pTarget).isTamed();
    }

    @Override
    public boolean isTeammate(Entity pEntity) {
        if (this.isTamed() && this.getOwner() != null) {
            return pEntity == this.getOwner();
        } else {
            return super.isTeammate(pEntity);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        sitEntity(tag.getBoolean("isSitting"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putBoolean("isSitting", this.isSitting());
    }

    // Método para que el Brotecito pueda sentarse y levantarse
    // También comprueba si está atacando y funcione correctamente la animación de ataque
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SITTING, false);
        this.dataTracker.startTracking(ATTACKING, false);
    }

    public boolean isAttacking() {
        return this.dataTracker.get(ATTACKING);
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
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(125.0D);
            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(20D);
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25f);
        } else {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(100.0D);
            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(15D);
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25f);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (this.isInSittingPose()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito_mamado.sit", Animation.LoopType.HOLD_ON_LAST_FRAME));
            return PlayState.CONTINUE;
        }

        if (this.isAttacking()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito_mamado.attack", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }

        if (tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito_mamado.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (!tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito_mamado.idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setOwner(PlayerEntity player) {
        this.setTamed(true);
        this.setOwnerUuid(player.getUuid());
    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }
}
