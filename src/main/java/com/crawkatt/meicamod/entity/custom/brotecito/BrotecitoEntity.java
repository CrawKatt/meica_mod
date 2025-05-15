package com.crawkatt.meicamod.entity.custom.brotecito;

import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.meica.MeicaEntity;
import com.crawkatt.meicamod.item.ModItems;
import com.crawkatt.meicamod.particle.ModParticles;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class BrotecitoEntity extends TamableAnimal implements NeutralMob, GeoEntity {
    @Nullable
    private UUID persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = UniformInt.of(20, 39);
    private int evolutionProgress = 0;
    private static final int MAX_EVOLUTION_PROGRESS = 5;
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(BrotecitoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(BrotecitoEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean aggressiveMode = false;

    public BrotecitoEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new BrotecitoBreedGoal(this, 1.0));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F) {
            @Override
            public boolean canUse() {
                return !BrotecitoEntity.this.isSitting() && super.canUse();
            }
        });
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this) {
            @Override
            public boolean canUse() {
                return !BrotecitoEntity.this.isSitting() && super.canUse();
            }
        });
        if (this.isAgressiveMode()) {
            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        } else {
            this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
            this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
            this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
            this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
            this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, false));
            this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
        }
    }

    public static AttributeSupplier createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.1f)
                .add(Attributes.ATTACK_KNOCKBACK, 2f)
                .add(Attributes.ATTACK_DAMAGE, 2f)
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
    public AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        return null;
    }

    // Método para que los Brotecitos puedan emitir partículas personalizadas al aparearse
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 18) {
            spawnBreedingParticles();
        } else if (id == 19 || id == 20) {
            spawnSitParticles();
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void spawnBreedingParticles() {
        for(int i = 0; i < 7; i++) {
            double d0 = this.random.nextGaussian() * 0.02;
            double d1 = this.random.nextGaussian() * 0.02;
            double d2 = this.random.nextGaussian() * 0.02;
            this.level().addParticle(ModParticles.KAPPA_PRIDE_PARTICLES.get(), this.getRandomX(1.0),this.getRandomY() + 0.5, this.getRandomZ(1.0), d0, d1, d2);
        }
    }

    public void spawnSitParticles() {
        BlockState dirtState = Blocks.DIRT.defaultBlockState();
        ParticleOptions particleOptions = new BlockParticleOption(ParticleTypes.BLOCK, dirtState);
        for (int i = 0; i < 15; ++i) {
            double offsetX = (this.random.nextDouble() - 0.5) * 0.5;
            double offsetY = 0.3;
            double offsetZ = (this.random.nextDouble() - 0.5) * 0.5;

            this.level().addParticle(
                    particleOptions,
                    this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ,
                    0.0, 0.1, 0.0
            );
        }
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.CARROT);
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

    // Método para que el Brotecito pueda ser domado con manzanas y evolucionar con Polvo de Brotenita
    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();

        Item itemForTaming = Items.APPLE;
        if (isFood(itemStack)) {
            return super.mobInteract(player, hand);
        }

        if (item == itemForTaming && !isTame()) {
            return this.tame(itemStack, player, hand);
        }

        if (isTame() && item instanceof SwordItem) {
            if (!this.level().isClientSide) {
                ItemStack copy = itemStack.copy();
                copy.setCount(1);
                this.setItemSlot(EquipmentSlot.MAINHAND, copy);
            }

            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }

        if (isTame()) {
            if (item == ModItems.BROTENITA_MEAL.get()) {
                return this.evolve(player, itemStack);
            } else if (!this.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
                this.setSitting(!this.isSitting());
                return InteractionResult.SUCCESS;
            }
        }

        if (itemStack.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    private InteractionResult tame(ItemStack itemStack, Player player, InteractionHand hand) {
        if (isFood(itemStack)) {
            return super.mobInteract(player, hand);
        }

        if (this.level().isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            if (!ForgeEventFactory.onAnimalTame(this, player)) {
                if (!this.level().isClientSide) {
                    super.tame(player);
                    this.navigation.recomputePath();
                    this.setTarget(null);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                    setSitting(false);
                }
            }

            return InteractionResult.SUCCESS;
        }
    }

    // Método para evolucionar al Brotecito
    private InteractionResult evolve(Player player, ItemStack itemStack) {
        if (this.level().isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            this.increaseEvolutionProgress(1);
            if (this.getEvolutionProgress() >= this.getMaxEvolutionProgress()) {
                Level level = this.level();
                BrotecitoMamadoEntity brotecitoMamado = new BrotecitoMamadoEntity(ModEntities.BROTECITO_MAMADO.get(), level);

                brotecitoMamado.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                brotecitoMamado.setTame(true);
                brotecitoMamado.setOwner(player);

                level.addFreshEntity(brotecitoMamado);
                this.discard();

                return InteractionResult.SUCCESS;
            }

            return InteractionResult.SUCCESS;
        }
    }

    // Método para que el Brotecito pueda atacar a entidades hostiles excepto a:
    // - Ghasts
    // - Brotecitos que no son suyos
    // - jugadores que no pueden ser dañados
    @Override
    public boolean wantsToAttack(@NotNull LivingEntity pTarget, @NotNull LivingEntity pOwner) {
        if (!(pTarget instanceof Ghast)) {
            if (pTarget instanceof BrotecitoEntity brotecitoEntity) {
                return !brotecitoEntity.isTame() || brotecitoEntity.getOwner() != pOwner;
            } else if (pTarget instanceof Player && pOwner instanceof Player && !((Player)pOwner).canHarmPlayer((Player)pTarget)) {
                return false;
            } else if (pTarget instanceof AbstractHorse && ((AbstractHorse)pTarget).isTamed()) {
                return false;
            } else {
                return !(pTarget instanceof TamableAnimal) || !((TamableAnimal)pTarget).isTame();
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity pEntity) {
        if (isAggressive() || !this.isTamed() && pEntity instanceof MeicaEntity) {
            return true;
        }

        if (this.isTame() && this.getOwner() != null) {
            return pEntity == this.getOwner();
        } else {
            return super.isAlliedTo(pEntity);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSitting(tag.getBoolean("isSitting"));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
    }

    // Método para que el Brotecito pueda sentarse y levantarse
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
    }

    public void setSitting(boolean sitting) {
        if (this.isSitting() != sitting) {
            this.entityData.set(SITTING, sitting);
            this.setOrderedToSit(sitting);
            this.playSound(sitting ? SoundEvents.GRAVEL_BREAK : SoundEvents.GRASS_BREAK, 1.0F, 0.8F);
            this.level().broadcastEntityEvent(this, sitting ? (byte) 19 : (byte) 20);
        }
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    @Override
    public Team getTeam() {
        return super.getTeam();
    }

    public boolean canBeLeashed(@NotNull Player player) {
        return false;
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(60.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25f);
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

    public boolean isTamed() {
        return this.isTame();
    }
}
