package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.MeicaEntity;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class MeicaRenderer extends BipedEntityRenderer<MeicaEntity, MeicaModel<MeicaEntity>> {
    public static final Identifier TEXTURE = new Identifier(MeicaMod.MOD_ID, "textures/entity/meica.png");

    public MeicaRenderer(EntityRendererFactory.Context context) {
        super(context, new MeicaModel<>(context.getPart(ModModelLayers.MEICA_LAYER)), 0.5F);
    }

    @Override
    public Identifier getTexture(MeicaEntity entity) {
        return TEXTURE;
    }
}