package com.example.examplemod.entity.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.BrotecitoEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BrotecitoRenderer extends MobRenderer<BrotecitoEntity, BrotecitoModel<BrotecitoEntity>> {
    public BrotecitoRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BrotecitoModel<>(pContext.bakeLayer(ModModelLayers.BROTECITO_LAYER)), 0.6f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BrotecitoEntity pEntity) {
        return new ResourceLocation(ExampleMod.MODID, "textures/entity/brotecito.png");
    }

    @Override
    public void render(BrotecitoEntity pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pMatrixStack,
                       @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            pMatrixStack.scale(0.3f, 0.3f, 0.3f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
