package com.example.examplemod.entity.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.meica.MeicaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MeicaRenderer extends HumanoidMobRenderer<MeicaEntity, MeicaModel<MeicaEntity>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/meica.png");

    public MeicaRenderer(EntityRendererProvider.Context context) {
        super(context, new MeicaModel<>(context.bakeLayer(MeicaModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(MeicaEntity entity) {
        return TEXTURE;
    }
}
