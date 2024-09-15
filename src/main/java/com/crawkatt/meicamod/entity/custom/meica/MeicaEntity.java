package com.crawkatt.meicamod.entity.custom.meica;

import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.brotecito.BrotecitoEntity;
import com.crawkatt.meicamod.entity.projectile.IceArrow;
import com.crawkatt.meicamod.entity.projectile.PoisonArrow;
import com.crawkatt.meicamod.item.ModItems;
import com.crawkatt.meicamod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.NotNull;

public class MeicaEntity extends Monster implements RangedAttackMob {
    private boolean isCamouflaged = false;
    private int camouflageCooldown = 0;
    private int camouflagedDuration = 0;
    /* Boss Bar */
    private final ServerBossEvent bossEvent =
            new ServerBossEvent(Component.literal("Meica"), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_12);

    public MeicaEntity(EntityType<? extends Monster> entityType, Level pLevel) {
        super(entityType, pLevel);
    }

    // Método para definir el equipo de Meica (arco por defecto)
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource src, @NotNull DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MEICA_BOW.get()));
    }

    @Override
    public void tick() {
        super.tick();

        if (camouflageCooldown > 0) {
            camouflageCooldown--;
            return;
        }

        if (isCamouflaged) {
            camouflagedDuration--;

            if (this.getHealth() < this.getMaxHealth()) {
                this.heal(0.30F);
            }

            if (camouflagedDuration <= 0) {
                this.removeEffect(MobEffects.INVISIBILITY);
                isCamouflaged = false;
            }
        }

        if (!this.isAlive()) {
            return;
        }

        // Invocar a Meica con un arco por defecto
        this.populateDefaultEquipmentSlots(this.random, this.level().getCurrentDifficultyAt(this.blockPosition()));
    }

    public boolean isCamouflaged() {
        return this.isCamouflaged;
    }

    public void activateCamouflage() {
        if (isCamouflaged || camouflageCooldown > 0) return;
        isCamouflaged = true;
        camouflagedDuration = 200;

        SimpleParticleType particle = ParticleTypes.SPORE_BLOSSOM_AIR;
        for (int i = 0; i < 100; i++) {
            double offsetX = (this.random.nextDouble() - 0.5) * 2.0;
            double offsetY = this.random.nextDouble() * 2.0;
            double offsetZ = (this.random.nextDouble() - 0.5) * 2.0;

            double speedX = (this.random.nextDouble() - 0.5) * 5.0;
            double speedY = this.random.nextDouble() * 5.0;
            double speedZ = (this.random.nextDouble() - 0.5) * 5.0;

            this.level().addParticle(particle, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ, speedX, speedY, speedZ);
        }

        this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, camouflagedDuration, 0, false, false));

        // Intentar encontrar una posición alejada del jugador y moverse allí
        for (int i = 0; i < 10; i++) {
            Vec3 pos = LandRandomPos.getPosAway(this, 16, 7, Vec3.atBottomCenterOf(this.blockPosition()));

            if (pos != null && isSafeTeleportPosition(pos) && this.getTarget() != null) {
                spawnBrotecitosAround(this.getTarget());
                this.teleportTo(pos.x(), pos.y(), pos.z());
                camouflageCooldown = 400;

                break;
            }
        }
    }

    private void spawnBrotecitosAround(LivingEntity targetPlayer) {
        if (this.level() instanceof ServerLevel serverLevel) {
            BlockPos playerPos = targetPlayer.blockPosition();
            for (int i = 0; i < 3; i++) {
                double offsetX = serverLevel.random.nextInt(10) - 5;
                double offsetZ = serverLevel.random.nextInt(10) - 5;

                BlockPos spawnPos = new BlockPos((int) (playerPos.getX() + offsetX), playerPos.getY(), (int) (playerPos.getZ() + offsetZ));

                if (isSafeSpawnPosition(spawnPos)) {
                    BrotecitoEntity brotecito = new BrotecitoEntity(ModEntities.BROTECITO.get(), this.level());
                    brotecito.moveTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), this.getYRot(), this.getXRot());
                    brotecito.setAggressiveMode(true);
                    brotecito.setTarget(targetPlayer);

                    this.level().addFreshEntity(brotecito);
                }
            }
        }
    }

    // Verificar que el bloque en la posición y el de arriba estén vacíos (sin colisiones)
    private boolean isSafeSpawnPosition(BlockPos pos) {
        return this.level().getBlockState(pos).isAir() &&
                this.level().getBlockState(pos.above()).isAir() &&
                this.level().noCollision(new AABB(pos));  // Verifica que no haya colisiones
    }

    private boolean isSafeTeleportPosition(Vec3 pos) {
        BlockPos blockPos = new BlockPos((int) pos.x(), (int) pos.y(), (int) pos.z());

        return this.level().getBlockState(blockPos).isAir() &&
                this.level().getBlockState(blockPos.above()).isAir() &&
                this.level().noCollision(this, this.getBoundingBox().move(pos));
    }

    // Método para realizar ataques a distancia
    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        Arrow arrow = getRandomArrowType(this.level(), this);
        double d0 = target.getX() - this.getX();
        double d1 = target.getBoundingBox().minY + (double)(target.getBbHeight() / 3.0F) - arrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        arrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(arrow);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MEICA_BOW.get()));
    }

    private Arrow getRandomArrowType(Level level, LivingEntity shooter) {
        RandomSource random = shooter.getRandom();
        int arrowType = random.nextInt(2);

        return switch (arrowType) {
            case 0 -> new PoisonArrow(level, shooter);
            case 1 -> new IceArrow(level, shooter);
            default -> new Arrow(level, shooter);
        };
    }

    // Método para definir los atributos de Meica (vida, daño, velocidad, etc.)
    public static AttributeSupplier createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .build();
    }

    // Define el comportamiento de la IA de Meica
    @Override
    // Define el comportamiento de Meica en el juego (atacar, seguir al jugador, etc.)
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(2, new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F));
        this.goalSelector.addGoal(3, new AvoidPlayerWhileCamouflagedGoal(this, 1.0D, 1.5D, 10.0D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
    }

    /* Boss Bar*/

    // Método para añadir el "evento jefe" al jugador (Muestra la barra de progreso cuando el jugador ve a Meica)
    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer pServerPlayer) {
        super.startSeenByPlayer(pServerPlayer);
        this.bossEvent.addPlayer(pServerPlayer);
    }

    // Método para remover el "evento jefe" del jugador (Quita la barra de progreso cuando el jugador o Meica muere)
    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer pServerPlayer) {
        super.stopSeenByPlayer(pServerPlayer);
        this.bossEvent.removePlayer(pServerPlayer);
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        //if (pSource.is(DamageTypeTags.IS_EXPLOSION) || pSource.is(DamageTypeTags.IS_FALL) || this.hasEffect(MobEffects.POISON)) return false;
        if (this.getHealth() < this.getMaxHealth() * 0.5 && !isCamouflaged) {
            if (this.getTarget() != null) {
                this.getTarget().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 1, false, false));
            }

            this.activateCamouflage();
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean isAlliedTo(@NotNull Team pTeam) {
        // Si hay Brotecitos no domesticados cerca, Meica es aliada
        if (this.level() instanceof ServerLevel serverLevel) {
            AABB brotecitosAround = this.getBoundingBox().inflate(10.0D);
            return serverLevel.getEntitiesOfClass(BrotecitoEntity.class, brotecitosAround).stream()
                    .anyMatch(brotecito -> !brotecito.isTamed());
        }

        return super.isAlliedTo(pTeam);
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
        return selectRandomDeathSound();
    }

    private SoundEvent selectRandomDeathSound() {
        return switch (this.random.nextInt(3)) {
            case 0 -> ModSounds.MEICA_DEATH.get();
            case 1 -> ModSounds.MEICA_DEATH_2.get();
            default -> ModSounds.MEICA_DEATH_3.get();
        };
    }

    // Método para reproducir un sonido cuando Meica mata a una entidad
    public void playKillSound() {
        this.level().playSeededSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.MEICA_KILL_ENTITY.get(), this.getSoundSource(), 1.0F, 1.0F, this.random.nextLong());
    }
}
