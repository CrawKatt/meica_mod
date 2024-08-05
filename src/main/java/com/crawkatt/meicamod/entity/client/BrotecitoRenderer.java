package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.brotecito.BrotecitoEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import java.util.Objects;

public class BrotecitoRenderer extends GeoEntityRenderer<BrotecitoEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MeicaMod.MODID, "textures/entity/brotecito.png");
    public BrotecitoRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BrotecitoModel());
        this.shadowRadius = 0.6f; // Tamaño de la sombra de la Entidad

        this.addRenderLayer(new BlockAndItemGeoLayer<>(this, (bone, animatable) -> {
            if (Objects.equals(bone.getName(), "rightArm")) //right hand
                return animatable.getItemInHand(InteractionHand.MAIN_HAND);
            return null;
        }, (bone, animatable) -> null) {
            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, BrotecitoEntity animatable) {
                return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
            }

            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, BrotecitoEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                // Traslación del objeto
                poseStack.translate(bone.getPosX(), -bone.getPosY() - 0.20F, bone.getPosZ() - 0.15F);

                // Rotación del objeto
                poseStack.mulPose(Axis.XP.rotationDegrees(bone.getRotX() - 40.5F)); // Ok (-40.5)

                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BrotecitoEntity pEntity) {
        return TEXTURE;
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
