package com.example.examplemod.entity.custom;

import com.example.examplemod.entity.ModEntityTypes;
import com.example.examplemod.entity.variant.BrotecitoVariant;
import com.example.examplemod.particle.ModParticles;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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

public class BrotecitoEntity extends TamableAnimal implements IAnimatable, RangedAttackMob {
    private AnimationFactory factory = new AnimationFactory(this);

    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(BrotecitoEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(BrotecitoEntity.class, EntityDataSerializers.INT);

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

        if (!this.hasItemInSlot(EquipmentSlot.OFFHAND)) {
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.ARROW, 1));
        }

        List<ItemEntity> itemsNearby = this.level.getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(0));

        for (ItemEntity itemEntity : itemsNearby) {
            Item item = itemEntity.getItem().getItem();
            if (item == Items.DIAMOND_SWORD || item == Items.BOW) {
                // Recoger el Objeto
                this.take(itemEntity, itemEntity.getItem().getCount());

                // Equipar la espada
                this.setItemSlot(EquipmentSlot.MAINHAND, itemEntity.getItem());
                itemEntity.discard();
                break;
            }
        }
    }

    // Método para que el Brotecito pueda atacar a distancia
    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        Arrow arrow = new Arrow(this.level, this);
        double d0 = target.getX() - this.getX();
        double d1 = target.getBoundingBox().minY + (double)(target.getBbHeight() / 3.0F) - arrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        arrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
    }

    // Método para definir los atributos del Brotecito (vida, daño, velocidad, etc.)
    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.00)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .build();
    }

    // Define el comportamiento de los Brotecitos en el juego (atacar, seguir al jugador, etc.)
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.setAttackGoals(); // Método personalizado para intercambiar entre ataque a distancia y cuerpo a cuerpo
    }

    private void setAttackGoals() {
        // Verificar si el Brotecito tiene un arco equipado
        if (this.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.BOW) {
            // Si tiene un arco, priorizar el ataque a distancia
            this.goalSelector.addGoal(1, new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F));
            this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        } else {
            // Si no tiene un arco, priorizar el ataque cuerpo a cuerpo
            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
            this.goalSelector.addGoal(2, new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F));
        }
    }

    // Define el comportamiento de los Brotecitos al atacar a otros mobs e impide que los Brotecitos domesticados se ataquen entre sí
    @Override
    public boolean canAttack(LivingEntity target) {
        // Si el objetivo es una instancia de BrotecitoEntity
        if (target instanceof BrotecitoEntity) {
            BrotecitoEntity brotecitoTarget = (BrotecitoEntity) target;

            // Si ambos Brotecitos tienen el mismo dueño, no pueden atacarse
            if (this.getOwner() != null && this.getOwner().equals(brotecitoTarget.getOwner())) {
                return false;
            }
        }

        // Para cualquier otro caso, se utiliza el comportamiento predeterminado
        return super.canAttack(target);
    }

    // Método para aparear a los Brotecitos y generar un nuevo Brotecito con una variante aleatoria
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        BrotecitoEntity baby = ModEntityTypes.BROTECITO.get().create(serverLevel);
        BrotecitoVariant variant = Util.getRandom(BrotecitoVariant.values(), this.random);
        baby.setVariant(variant);

        return baby;
    }

    // Método para que los Brotecitos puedan emitir partículas personalizadas al aparearse
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 18) {
            for(int i = 0; i < 7; ++i) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                this.level.addParticle(ModParticles.KAPPA_PRIDE_PARTICLES.get(), this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d0, d1, d2);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.getItem() == Items.CARROT;
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

        if(isFood(itemstack)) {
            return super.mobInteract(player, hand);
        }

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
        tag.putInt("Variant", this.getTypeVariant());
    }

    // Método para que el Brotecito pueda sentarse y levantarse
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
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

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(
            ServerLevelAccessor p_146746,
            DifficultyInstance p_146747_,
            MobSpawnType p_146748_,
            @Nullable SpawnGroupData p_146749_,
            @Nullable CompoundTag p_146750_
    ) {
        BrotecitoVariant variant = BrotecitoVariant.getRandomVariant();
        setVariant(variant);
        return super.finalizeSpawn(p_146746, p_146747_, p_146748_, p_146749_, p_146750_);
    }

    public BrotecitoVariant getVariant() {
        return BrotecitoVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(BrotecitoVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }
}
