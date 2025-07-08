package com.crawkatt.meicamod.entity.custom;

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
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ZeroFiveEntity extends HostileEntity implements RangedAttackMob {
    /* Boss Bar */
    private final ServerBossBar bossEvent =
            new ServerBossBar(Text.literal("05"), BossBar.Color.GREEN, BossBar.Style.NOTCHED_12);

    public ZeroFiveEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.isAlive()) {
            return;
        }

        if (this.getY() < 20) {
            escapeVoid();
        }
    }

    private void escapeVoid() {
        World world = this.getWorld();
        PlayerEntity nearestPlayer = world.getClosestPlayer(this, 100);

        if (nearestPlayer != null) {
            Vec3d playerPos = nearestPlayer.getPos();
            Vec3d zeroFivePos = this.getPos();

            if (!nearestPlayer.getAbilities().flying || !nearestPlayer.getAbilities().creativeMode) {
                this.teleport(playerPos.x, playerPos.y, playerPos.z);
                nearestPlayer.teleport(zeroFivePos.x, zeroFivePos.y, zeroFivePos.z);
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

    private boolean isSafeTeleportPosition(Vec3d pos) {
        BlockPos blockPos = new BlockPos((int) pos.x, (int) pos.y, (int) pos.z);

        return this.getWorld().getBlockState(blockPos).isAir() &&
                this.getWorld().getBlockState(blockPos.up()).isAir() &&
                this.getWorld().isSpaceEmpty(this, this.getBoundingBox().offset(pos));
    }

    // Método para realizar ataques a distancia
    @Override
    public void attack(LivingEntity target, float pullProgress) {
        Vec3d vec3d = target.getVelocity();
        double d = target.getX() + vec3d.x - this.getX();
        double e = target.getEyeY() - (double)1.1f - this.getY();
        double f = target.getZ() + vec3d.z - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        Potion potion = Potions.HARMING;
        if (target instanceof RaiderEntity) {
            potion = target.getHealth() <= 4.0f ? Potions.HEALING : Potions.REGENERATION;
            this.setTarget(null);
        } else if (g >= 8.0 && !target.hasStatusEffect(StatusEffects.SLOWNESS)) {
            potion = Potions.SLOWNESS;
        } else if (target.getHealth() >= 8.0f && !target.hasStatusEffect(StatusEffects.POISON)) {
            potion = Potions.POISON;
        } else if (g <= 3.0 && !target.hasStatusEffect(StatusEffects.WEAKNESS) && this.random.nextFloat() < 0.25f) {
            potion = Potions.WEAKNESS;
        }
        PotionEntity potionEntity = new PotionEntity(this.getWorld(), this);
        potionEntity.setItem(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), potion));
        potionEntity.setPitch(potionEntity.getPitch() - -20.0f);
        potionEntity.setVelocity(d, e + g * 0.2, f, 0.75f, 8.0f);
        if (!this.isSilent()) {
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0f, 0.8f + this.random.nextFloat() * 0.4f);
        }
        this.getWorld().spawnEntity(potionEntity);
    }

    // Método para definir los atributos de 05 (vida, daño, velocidad, etc.)
    public static DefaultAttributeContainer createZeroFiveAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 500.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0D)
                .build();
    }

    // Define el comportamiento de la IA de 05
    @Override
    // Define el comportamiento de 05 en el juego (atacar, seguir al jugador, etc.)
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.0D, 60, 10.0F));
        this.goalSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(10, new LookAroundGoal(this));
    }

    /* Boss Bar*/

    // Método para añadir el "evento jefe" al jugador (Muestra la barra de progreso cuando el jugador ve a 05)
    @Override
    public void onStartedTrackingBy(@NotNull ServerPlayerEntity pServerPlayer) {
        super.onStartedTrackingBy(pServerPlayer);
        this.bossEvent.addPlayer(pServerPlayer);
    }

    // Método para remover el "evento jefe" del jugador (Quita la barra de progreso cuando el jugador o 05 muere)
    @Override
    public void onStoppedTrackingBy(@NotNull ServerPlayerEntity pServerPlayer) {
        super.onStoppedTrackingBy(pServerPlayer);
        this.bossEvent.removePlayer(pServerPlayer);
    }

    @Override
    public boolean damage(@NotNull DamageSource pSource, float pAmount) {
        if (pSource.isIn(DamageTypeTags.IS_EXPLOSION) || pSource.isIn(DamageTypeTags.IS_FALL) || this.hasStatusEffect(StatusEffects.POISON)) return false;
        return super.damage(pSource, pAmount);
    }

    @Override
    public boolean isTeammate(Entity pTeam) {
        // Si hay Brotecitos no domesticados cerca, 05 es aliada
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

    // Método para reproducir un sonido cuando 05 mata a una entidad
    public void playKillSound() {
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), selectRandomKillSound(), this.getSoundCategory(), 1.0F, 1.0F, this.random.nextLong());
    }
}
