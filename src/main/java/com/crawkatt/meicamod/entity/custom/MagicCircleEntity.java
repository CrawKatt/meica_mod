package com.crawkatt.meicamod.entity.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BowItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MagicCircleEntity extends Entity {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public MagicCircleEntity(EntityType<? extends MagicCircleEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (client.player == null || !client.player.isUsingItem() ||
                !(client.player.getActiveItem().getItem() instanceof BowItem)) {
            this.discard();
            return;
        }

        Vec3d eyePos = client.player.getCameraPosVec(1.0F);
        Vec3d lookVec = client.player.getRotationVec(1.0F);
        Vec3d targetPos = eyePos.add(lookVec.multiply(2.5));

        this.setPosition(targetPos.x, targetPos.y, targetPos.z);
    }

    @Override
    protected void initDataTracker() {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        throw new UnsupportedOperationException("MagicCircleEntity is ClientSide only and should not be spawned on server");
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.fixed(0.01f, 0.01f);
    }
}
