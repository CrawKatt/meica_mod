package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.ZeroFiveEntity;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class ZeroFiveRenderer extends BipedEntityRenderer<ZeroFiveEntity, ZeroFiveModel<ZeroFiveEntity>> {
    public static final Identifier TEXTURE = new Identifier(MeicaMod.MOD_ID, "textures/entity/05.png");

    public ZeroFiveRenderer(EntityRendererFactory.Context context) {
        super(context, new ZeroFiveModel<>(context.getPart(ModModelLayers.ZERO_FIVE_LAYER)), 0.5F);
    }

    @Override
    public Identifier getTexture(ZeroFiveEntity entity) {
        return TEXTURE;
    }
}