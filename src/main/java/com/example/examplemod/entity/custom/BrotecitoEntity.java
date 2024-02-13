package com.example.examplemod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class BrotecitoEntity extends TamableAnimal implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);

    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(BrotecitoEntity.class, EntityDataSerializers.BOOLEAN);

    public BrotecitoEntity(EntityType<? extends TamableAnimal> entityTpe, Level level) {
        super(entityTpe, level);
    }

    // Método para que el Brotecito pueda obtener espadas de diamante y equiparlas
    @Override
    public void tick() {
        super.tick();

        if (!this.isAlive()) {
            return;
        }

        List<ItemEntity> itemsNearby = this.level.getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(0));

        for (ItemEntity itemEntity : itemsNearby) {
            if (itemEntity.getItem().getItem() == Items.DIAMOND_SWORD) {
                // Recoger la espada
                this.take(itemEntity, itemEntity.getItem().getCount());

                // Equipar la espada
                this.setItemSlot(EquipmentSlot.MAINHAND, itemEntity.getItem());
                itemEntity.discard();
                break;
            }
        }
    }

    // Método para definir los atributos del Brotecito (vida, daño, velocidad, etc.)
    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.00)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .build();
    }

    // Define el comportamiento de los Brotecitos en el juego (atacar, seguir al jugador, etc.)
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(6, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    private<E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.brotecito_entity.walk", true));
            return PlayState.CONTINUE;
        }

        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.brotecito_entity.sitting", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.brotecito_entity.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.CAT_STRAY_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.DOLPHIN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.DOLPHIN_DEATH;
    }

    protected float getSoundVolume() {
        return 0.2F;
    }

    /* TAMEABLE */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        Item itemForTaming = Items.APPLE;

        if (item == itemForTaming && !isTame()) {
            if (this.level.isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (!ForgeEventFactory.onAnimalTame(this, player)) {
                    if (!this.level.isClientSide) {
                        super.tame(player);
                        this.navigation.recomputePath();
                        this.setTarget(null);
                        this.level.broadcastEntityEvent(this, (byte)7);
                        setSitting(false);
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        if(isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        if (itemstack.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
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
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
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

    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(60.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.5f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.25f);
        }
    }
}
