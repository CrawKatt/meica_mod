package com.example.examplemod.entity.custom.brotecito;

import com.example.examplemod.entity.ModEntities;
import com.example.examplemod.particle.ModParticles;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class BrotecitoEntity extends TamableAnimal implements NeutralMob {
    @Nullable
    private UUID persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = UniformInt.of(20, 39);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(BrotecitoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(BrotecitoEntity.class, EntityDataSerializers.BOOLEAN);

    public BrotecitoEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // Método para que el Brotecito pueda obtener espadas de diamante y equiparlas
    @Override
    public void tick() {
        super.tick();

        if (!this.isAlive()) {
            return;
        }

        /* Añadir flechas en caso de que el Brotecito sea arquero
        if (!this.hasItemInSlot(EquipmentSlot.OFFHAND)) {
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.ARROW, 1));
        }
        */

        List<ItemEntity> itemsNearby = this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(1.0));

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
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0));
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

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.1f)
                .add(Attributes.ATTACK_KNOCKBACK, 2f)
                .add(Attributes.ATTACK_DAMAGE, 2f);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        return ModEntities.BROTECITO.get().create(pLevel);
    }

    // Método para que los Brotecitos puedan emitir partículas personalizadas al aparearse
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 18) {
            for(int i = 0; i < 7; i++) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                this.level().addParticle(ModParticles.KAPPA_PRIDE_PARTICLES.get(), this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d0, d1, d2);
            }
        } else {
            super.handleEntityEvent(id);
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

    /* TAMEABLE */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();

        Item itemForTaming = Items.APPLE;

        if (isFood(itemStack)) {
            return super.mobInteract(player, hand);
        }

        if (item == itemForTaming && !isTame()) {
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

        if (isTame() && !this.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        if (itemStack.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }
        
        return super.mobInteract(player, hand);
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
