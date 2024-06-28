package com.example.examplemod.entity.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.brotecito.BrotecitoEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BrotecitoRenderer extends MobRenderer<BrotecitoEntity, BrotecitoModel<BrotecitoEntity>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/brotecito.png");
    public BrotecitoRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BrotecitoModel<>(pContext.bakeLayer(ModModelLayers.BROTECITO_LAYER)), 0.6f);
        this.addLayer(new ItemInHandLayer<>(this, pContext.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BrotecitoEntity pEntity) {
        return TEXTURE;
    }

/*
    @Override
    public void render(BrotecitoEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        if (!entity.isInvisible()) {
            poseStack.pushPose();

            // Ajusta la posición del ítem en la mano derecha
            poseStack.translate(0.0D, 1.2D, -0.2D); // Ajusta la posición según sea necesario
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));

            ModelPart armRight = this.getModel().armRight;
            armRight.translateAndRotate(poseStack);

            ItemStack itemstack = entity.getMainHandItem();
            if (itemstack.isEmpty()) {
                itemstack = new ItemStack(Items.STONE_SWORD);
            }

            Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
            poseStack.popPose();
        }
    }
    */
    @Override
    public void render(BrotecitoEntity pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pMatrixStack,
                       @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            pMatrixStack.scale(0.3f, 0.3f, 0.3f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
