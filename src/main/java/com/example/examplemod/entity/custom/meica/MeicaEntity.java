package com.example.examplemod.entity.custom.meica;

import com.example.examplemod.entity.custom.brotecito.BrotecitoEntity;
import com.example.examplemod.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static com.example.examplemod.sound.ModSounds.MEICA_DEATH;
import static com.example.examplemod.sound.ModSounds.MEICA_KILL_ENTITY;

public class MeicaEntity extends Monster implements NeutralMob, RangedAttackMob {
    @Nullable
    private UUID persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = UniformInt.of(20, 39);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(BrotecitoEntity.class, EntityDataSerializers.INT);
    /* Boss Bar */
    private final ServerBossEvent bossEvent =
            new ServerBossEvent(Component.literal("Meica"), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_12);

    public MeicaEntity(EntityType<? extends Monster> entityType, Level pLevel) {
        super(entityType, pLevel);
    }

    // Método para definir el equipo de Meica (arco por defecto)
    protected void populateDefaultEquipmentSlots(RandomSource src, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MEICA_BOW.get()));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.isAlive()) {
            return;
        }

        // Invocar a Meica con un arco por defecto
        this.populateDefaultEquipmentSlots(this.random, this.level().getCurrentDifficultyAt(this.blockPosition()));
    }

    // Método para realizar ataques a distancia
    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        Arrow arrow = new Arrow(this.level(), this);
        double d0 = target.getX() - this.getX();
        double d1 = target.getBoundingBox().minY + (double)(target.getBbHeight() / 3.0F) - arrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        arrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(arrow);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MEICA_BOW.get()));
    }

    // Método para definir los atributos de Meica (vida, daño, velocidad, etc.)
    public static AttributeSupplier createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.00)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513)
                .build();
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

    // Define el comportamiento de la IA de Meica
    @Override
    // Define el comportamiento de Meica en el juego (atacar, seguir al jugador, etc.)
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
    }

    /* Boss Bar*/

    // Método para añadir el "evento jefe" al jugador (Muestra la barra de progreso cuando el jugador ve a Meica)
    @Override
    public void startSeenByPlayer(ServerPlayer pServerPlayer) {
        super.startSeenByPlayer(pServerPlayer);
        this.bossEvent.addPlayer(pServerPlayer);
    }

    // Método para remover el "evento jefe" del jugador (Quita la barra de progreso cuando el jugador o Meica muere)
    @Override
    public void stopSeenByPlayer(ServerPlayer pServerPlayer) {
        super.stopSeenByPlayer(pServerPlayer);
        this.bossEvent.removePlayer(pServerPlayer);
    }

    // Actualiza la barra de progreso del jefe (Cuantos corazones le quedan)
    @Override
    public void aiStep() {
        super.aiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    /* SOUNDS */

    @Override
    protected SoundEvent getDeathSound() {
        return MEICA_DEATH.get();
    }

    // Método para reproducir un sonido cuando Meica mata a una entidad
    public void playKillSound() {
        this.level().playSeededSound(null, this.getX(), this.getY(), this.getZ(), MEICA_KILL_ENTITY.get(), this.getSoundSource(), 1.0F, 1.0F, this.random.nextLong());
    }
}
