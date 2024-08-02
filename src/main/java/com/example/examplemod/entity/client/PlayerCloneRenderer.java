package com.example.examplemod.entity.client;

import com.example.examplemod.entity.custom.player_clone.PlayerCloneEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PlayerCloneRenderer extends MobRenderer<PlayerCloneEntity, PlayerModel<PlayerCloneEntity>> {
    public PlayerCloneRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(PlayerCloneEntity entity) {
        return entity.getPlayerSkin();
    }
}
