package com.example.examplemod.entity.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.BrotecitoEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BrotecitoRenderer extends MobRenderer<BrotecitoEntity, BrotecitoModel<BrotecitoEntity>> {
    public BrotecitoRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BrotecitoModel<>(pContext.bakeLayer(ModModelLayers.BROTECITO_LAYER)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(BrotecitoEntity pEntity) {
        return new ResourceLocation(ExampleMod.MODID, "textures/entity/brotecito.png");
    }

    @Override
    public void render(BrotecitoEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
