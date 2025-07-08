package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.custom.MagicCircleEntity;
import com.crawkatt.meicamod.item.custom.MeicaBowItem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class UseItemEvent implements ClientTickEvents.EndTick {
    private boolean wasUsingItem = false;
    private MagicCircleEntity magicCircleEntity = null;

    @Override
    public void onEndTick(MinecraftClient client) {
        if (client.player != null && client.world != null) {
            boolean isUsingItem = client.player.isUsingItem();
            ItemStack stack = client.player.getActiveItem();

            if (isUsingItem && stack.getItem() instanceof MeicaBowItem) {
                if (!wasUsingItem && magicCircleEntity == null) {
                    magicCircleEntity = new MagicCircleEntity(ModEntities.MAGIC_CIRCLE, client.world);
                    magicCircleEntity.setNoGravity(true);
                    client.world.addEntity(-1, magicCircleEntity);
                }

                if (magicCircleEntity != null) {
                    Vec3d cameraPos = client.player.getCameraPosVec(1.0F);
                    Vec3d rotation = client.player.getRotationVec(1.0F);
                    Vec3d newPos = cameraPos.add(rotation.multiply(1.5));

                    magicCircleEntity.setPosition(newPos.x, newPos.y, newPos.z);
                }

            } else {
                if (wasUsingItem && magicCircleEntity != null) {
                    magicCircleEntity.kill();
                    magicCircleEntity = null;
                }
            }

            wasUsingItem = isUsingItem && (stack.getItem() instanceof BowItem);
        }
    }
}
