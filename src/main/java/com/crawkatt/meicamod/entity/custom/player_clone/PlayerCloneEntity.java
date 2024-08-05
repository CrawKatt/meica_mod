package com.crawkatt.meicamod.entity.custom.player_clone;

import com.crawkatt.meicamod.util.SyncSkinManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class PlayerCloneEntity extends PathfinderMob {
    private ResourceLocation playerSkin;

    public PlayerCloneEntity(EntityType<? extends PathfinderMob> entityType, Level pLevel) {
        super(entityType, pLevel);
        this.setCustomNameVisible(true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.00)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513)
                .build();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.getTarget() instanceof Player player) {
            this.setCustomName(player.getName());
            UUID playerUUID = player.getUUID();
            String playerName = player.getName().getString();
            SyncSkinManager.get(playerName, playerUUID, skin -> this.playerSkin = skin);
        }
    }

    public ResourceLocation getPlayerSkin() {
        LocalPlayer skinLocation = Minecraft.getInstance().player;
        return skinLocation == null ? DefaultPlayerSkin.getDefaultSkin() : this.playerSkin != null ? this.playerSkin : skinLocation.getSkinTextureLocation();
    }
}
