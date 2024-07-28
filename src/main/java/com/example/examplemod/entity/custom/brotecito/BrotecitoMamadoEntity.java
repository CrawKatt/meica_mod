package com.example.examplemod.entity.custom.brotecito;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class BrotecitoMamadoEntity extends TamableAnimal implements NeutralMob, GeoEntity {
    @Nullable
    private UUID persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = UniformInt.of(20, 39);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(BrotecitoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(BrotecitoEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int attackAnimationTick;
    private static final int ATTACK_DURATION = 20;
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(BrotecitoMamadoEntity.class, EntityDataSerializers.BOOLEAN);

    public BrotecitoMamadoEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
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
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(5, new BrotecitoMamadoMeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, false));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    /* Define los attributos del Brotecito Mamado */
    public static AttributeSupplier createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100.D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.1f)
                .add(Attributes.ATTACK_KNOCKBACK, 5f)
                .add(Attributes.ATTACK_DAMAGE, 15f)
                .build();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        return null;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int i) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, i);
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public void startAttack() {
        this.entityData.set(ATTACKING, true);
        this.attackAnimationTick = ATTACK_DURATION;
    }

    public void stopAttack() {
        this.entityData.set(ATTACKING, false);
        this.attackAnimationTick = 0;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (isTame() && !this.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    // Método para que el Brotecito pueda atacar a entidades hostiles excepto a:
    // - Ghasts
    // - Brotecitos que no son suyos
    // - jugadores que no pueden ser dañados
    @Override
    public boolean wantsToAttack(@NotNull LivingEntity pTarget, @NotNull LivingEntity pOwner) {
        return !isNonAttackableTarget(pTarget, pOwner);
    }

    private boolean isNonAttackableTarget(LivingEntity pTarget, LivingEntity pOwner) {
        if (pTarget instanceof Ghast) {
            return true;
        }

        if (pTarget instanceof BrotecitoEntity brotecitoEntity) {
            return brotecitoEntity.isTame() && brotecitoEntity.getOwner() == pOwner;
        }

        if (pTarget instanceof BrotecitoMamadoEntity brotecitoMamadoEntity) {
            return brotecitoMamadoEntity.isTame() && brotecitoMamadoEntity.getOwner() == pOwner;
        }

        if (pTarget instanceof Player && pOwner instanceof Player) {
            return !((Player) pOwner).canHarmPlayer((Player) pTarget);
        }

        if (pTarget instanceof AbstractHorse) {
            return ((AbstractHorse) pTarget).isTamed();
        }

        return pTarget instanceof TamableAnimal && ((TamableAnimal) pTarget).isTame();
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (this.isTame() && this.getOwner() != null) {
            return pEntity == this.getOwner();
        } else {
            return super.isAlliedTo(pEntity);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSitting(tag.getBoolean("isSitting"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
    }

    // Método para que el Brotecito pueda sentarse y levantarse
    // También comprueba si está atacando y funcione correctamente la animación de ataque
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
        this.entityData.define(ATTACKING, false);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    @Override
    public Team getTeam() {
        return super.getTeam();
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(125.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(20D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(15D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25f);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (this.isAttacking()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito_mamado.attack", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        } else if (tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito_mamado.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        } else if (!tAnimationState.isMoving() && !this.isInSittingPose()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito_mamado.idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        } else if (this.isInSittingPose()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.brotecito_mamado.sit", Animation.LoopType.HOLD_ON_LAST_FRAME));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setOwner(Player player) {
        this.setTame(true);
        this.setOwnerUUID(player.getUUID());
    }
}
