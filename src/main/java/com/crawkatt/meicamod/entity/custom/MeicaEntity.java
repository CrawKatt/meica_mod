package com.crawkatt.meicamod.entity.custom;

import com.crawkatt.meicamod.effect.ModEffects;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.goal.AvoidPlayerWhileCamouflagedGoal;
import com.crawkatt.meicamod.entity.goal.MeicaBowAttackGoal;
import com.crawkatt.meicamod.entity.projectile.IceArrow;
import com.crawkatt.meicamod.entity.projectile.PoisonArrow;
import com.crawkatt.meicamod.item.ModItems;
import com.crawkatt.meicamod.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class MeicaEntity extends HostileEntity implements RangedAttackMob {
    /* Boss Bar */
    private final ServerBossBar bossEvent =
            new ServerBossBar(Text.literal("Meica"), BossBar.Color.GREEN, BossBar.Style.NOTCHED_12);

    public MeicaEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 1.0F);
    }

    // Método para definir el equipo de Meica (arco por defecto)
    @Override
    protected void initEquipment(@NotNull Random src, @NotNull LocalDifficulty difficulty) {
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MEICA_BOW));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.hasStatusEffect(StatusEffects.INVISIBILITY) && this.getHealth() < this.getMaxHealth()) {
            this.heal(0.30F);
        }

        if (this.getY() < 20) {
            escapeVoid();
        }

        if (!this.isAlive()) {
            return;
        }

        // Invocar a Meica con un arco por defecto
        this.initEquipment(this.random, this.getWorld().getLocalDifficulty(this.getBlockPos()));
    }

    private void escapeVoid() {
        World world = this.getWorld();
        PlayerEntity nearestPlayer = world.getClosestPlayer(this, 100);

        if (nearestPlayer != null) {
            Vec3d playerPos = nearestPlayer.getPos();
            Vec3d meicaPos = this.getPos();

            if (!nearestPlayer.getAbilities().flying || !nearestPlayer.getAbilities().creativeMode) {
                this.teleport(playerPos.x, playerPos.y, playerPos.z);
                nearestPlayer.teleport(meicaPos.x, meicaPos.y, meicaPos.z);
                nearestPlayer.playSound(ModSounds.MEICA_KILL_ENTITY_LAUGHT, this.getSoundCategory(), 1.0F, 1.0F);
            }

        } else {
            teleportToSafeLocation();
        }
    }

    private void teleportToSafeLocation() {
        for (int i = 0; i < 10; i++) {
            Vec3d startPos = this.getPos();
            Vec3d safePos = FuzzyTargeting.findFrom(this, 16, 7, startPos);

            if (safePos != null && isSafeTeleportPosition(safePos)) {
                this.teleport(safePos.x, safePos.y, safePos.z);
                spawnParticles();
                break;
            }
        }
    }

    public boolean isCamouflaged() {
        return this.hasStatusEffect(ModEffects.CAMOUFLAGE_COOLDOWN);
    }

    public void activateCamouflage() {
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 200, 0, false, false));
        spawnParticles();

        // Intentar encontrar una posición alejada del jugador y moverse allí
        for (int i = 0; i < 10; i++) {
            Vec3d pos = FuzzyTargeting.findFrom(this, 16, 7, Vec3d.ofBottomCenter(this.getBlockPos()));

            if (pos != null && isSafeTeleportPosition(pos) && this.getTarget() != null) {
                spawnBrotecitosAround(this.getTarget());
                this.teleport(pos.x, pos.y, pos.z);
                this.addStatusEffect(new StatusEffectInstance(ModEffects.CAMOUFLAGE_COOLDOWN, 400, 1, false, false));

                break;
            }
        }
    }

    private void spawnParticles() {
        DefaultParticleType particle = ParticleTypes.SPORE_BLOSSOM_AIR;
        for (int i = 0; i < 100; i++) {
            double offsetX = (this.random.nextDouble() - 0.5) * 2.0;
            double offsetY = this.random.nextDouble() * 2.0;
            double offsetZ = (this.random.nextDouble() - 0.5) * 2.0;

            double speedX = (this.random.nextDouble() - 0.5) * 5.0;
            double speedY = this.random.nextDouble() * 5.0;
            double speedZ = (this.random.nextDouble() - 0.5) * 5.0;

            this.getWorld().addParticle(particle, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ, speedX, speedY, speedZ);
        }
    }

    private void spawnBrotecitosAround(LivingEntity targetPlayer) {
        if (this.getWorld() instanceof ServerWorld serverLevel) {
            BlockPos playerPos = targetPlayer.getBlockPos();
            for (int i = 0; i < 3; i++) {
                double offsetX = serverLevel.random.nextInt(10) - 5;
                double offsetZ = serverLevel.random.nextInt(10) - 5;

                BlockPos spawnPos = new BlockPos((int) (playerPos.getX() + offsetX), playerPos.getY(), (int) (playerPos.getZ() + offsetZ));

                if (isSafeSpawnPosition(spawnPos)) {
                    BrotecitoEntity brotecito = new BrotecitoEntity(ModEntities.BROTECITO, this.getWorld());
                    brotecito.updatePosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                    brotecito.setAggressiveMode(true);
                    brotecito.setTarget(targetPlayer);

                    this.getWorld().spawnEntity(brotecito);
                }
            }
        }
    }

    // Verificar que el bloque en la posición y el de arriba estén vacíos (sin colisiones)
    private boolean isSafeSpawnPosition(BlockPos pos) {
        return this.getWorld().getBlockState(pos).isAir() &&
                this.getWorld().getBlockState(pos.up()).isAir() &&
                this.getWorld().isSpaceEmpty(new Box(pos));  // Verifica que no haya colisiones
    }

    private boolean isSafeTeleportPosition(Vec3d pos) {
        BlockPos blockPos = new BlockPos((int) pos.x, (int) pos.y, (int) pos.z);

        return this.getWorld().getBlockState(blockPos).isAir() &&
                this.getWorld().getBlockState(blockPos.up()).isAir() &&
                this.getWorld().isSpaceEmpty(this, this.getBoundingBox().offset(pos));
    }

    // Método para realizar ataques a distancia
    @Override
    public void attack(LivingEntity target, float distanceFactor) {
        ArrowEntity arrow = getRandomArrowType(this.getWorld(), this);
        double d0 = target.getX() - this.getX();
        double d1 = target.getBoundingBox().minY + (double)(target.getHeight() / 3.0F) - arrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        arrow.setVelocity(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.getWorld().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.getWorld().spawnEntity(arrow);
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MEICA_BOW));
    }

    private ArrowEntity getRandomArrowType(World level, LivingEntity shooter) {
        Random random = shooter.getRandom();
        int arrowType = random.nextInt(2);

        return switch (arrowType) {
            case 0 -> new PoisonArrow(level, shooter);
            case 1 -> new IceArrow(level, shooter);
            default -> new ArrowEntity(level, shooter);
        };
    }

    // Método para definir los atributos de Meica (vida, daño, velocidad, etc.)
    public static DefaultAttributeContainer createMeicaAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 500.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0D)
                .build();
    }

    // Define el comportamiento de la IA de Meica
    @Override
    // Define el comportamiento de Meica en el juego (atacar, seguir al jugador, etc.)
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeicaBowAttackGoal<>(this, 1.0D, 20, 30.0F));
        this.goalSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.goalSelector.add(3, new AvoidPlayerWhileCamouflagedGoal(this, 1, 1.5D, 10.0D));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(10, new LookAroundGoal(this));
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        if (!this.getWorld().isClient) {
            ItemStack meicaBow = this.getEquippedStack(EquipmentSlot.MAINHAND);
            meicaBow.setDamage(0);
            this.dropStack(meicaBow);
        }
    }

    /* Boss Bar*/

    // Método para añadir el "evento jefe" al jugador (Muestra la barra de progreso cuando el jugador ve a Meica)
    @Override
    public void onStartedTrackingBy(@NotNull ServerPlayerEntity pServerPlayer) {
        super.onStartedTrackingBy(pServerPlayer);
        this.bossEvent.addPlayer(pServerPlayer);
    }

    // Método para remover el "evento jefe" del jugador (Quita la barra de progreso cuando el jugador o Meica muere)
    @Override
    public void onStoppedTrackingBy(@NotNull ServerPlayerEntity pServerPlayer) {
        super.onStoppedTrackingBy(pServerPlayer);
        this.bossEvent.removePlayer(pServerPlayer);
    }

    @Override
    public boolean damage(@NotNull DamageSource pSource, float pAmount) {
        if (pSource.isIn(DamageTypeTags.IS_EXPLOSION) || pSource.isIn(DamageTypeTags.IS_FALL) || this.hasStatusEffect(StatusEffects.POISON)) return false;
        if (this.getHealth() < this.getMaxHealth() * 0.5 && !isCamouflaged()) {
            if (this.getTarget() != null) {
                this.getTarget().addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 1, false, false));
            }

            this.activateCamouflage();
        }
        return super.damage(pSource, pAmount);
    }

    @Override
    public boolean isTeammate(Entity pTeam) {
        // Si hay Brotecitos no domesticados cerca, Meica es aliada
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            Box brotecitosAround = this.getBoundingBox().expand(10.0D);
            return serverWorld.getEntitiesByClass(BrotecitoEntity.class, brotecitosAround, EntityPredicates.EXCEPT_SPECTATOR).stream()
                    .anyMatch(brotecito -> !brotecito.isEntityTamed());
        }

        return super.isTeammate(pTeam);
    }

    // Actualiza la barra de progreso del jefe (Cuantos corazones le quedan)
    @Override
    public void tickMovement() {
        super.tickMovement();
        this.bossEvent.setPercent(this.getHealth() / this.getMaxHealth());
    }

    /* SOUNDS */

    @Override
    public float getSoundPitch() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return selectRandomDeathSound();
    }

    private SoundEvent selectRandomDeathSound() {
        return switch (this.random.nextInt(3)) {
            case 0 -> ModSounds.MEICA_DEATH;
            case 1 -> ModSounds.MEICA_DEATH_2;
            default -> ModSounds.MEICA_DEATH_3;
        };
    }

    private SoundEvent selectRandomKillSound() {
        return switch (this.random.nextInt(3)) {
            case 0 -> ModSounds.MEICA_KILL_ENTITY;
            case 1 -> ModSounds.MEICA_KILL_ENTITY_LAUGHT;
            default -> ModSounds.MEICA_LAUGHT;
        };
    }

    // Método para reproducir un sonido cuando Meica mata a una entidad
    public void playKillSound() {
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), selectRandomKillSound(), this.getSoundCategory(), 1.0F, 1.0F, this.random.nextLong());
    }
}
