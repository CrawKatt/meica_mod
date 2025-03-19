package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.BrotecitoEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import java.util.Objects;

public class BrotecitoRenderer extends GeoEntityRenderer<BrotecitoEntity> {
    public static final Identifier TEXTURE = new Identifier(MeicaMod.MOD_ID, "textures/entity/brotecito.png");
    public BrotecitoRenderer(EntityRendererFactory.Context pContext) {
        super(pContext, new BrotecitoModel());
        this.shadowRadius = 0.6f; // Tamaño de la sombra de la Entidad

        this.addRenderLayer(new BlockAndItemGeoLayer<>(this, (bone, animatable) -> {
            if (Objects.equals(bone.getName(), "ir_rightArm")) //right hand
                return animatable.getStackInHand(Hand.MAIN_HAND);
            return null;
        }, (bone, animatable) -> null) {
            @Override
            protected ModelTransformationMode getTransformTypeForStack(GeoBone bone, ItemStack stack, BrotecitoEntity animatable) {
                return ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
            }

            @Override
            protected void renderStackForBone(MatrixStack poseStack, GeoBone bone, ItemStack stack, BrotecitoEntity animatable, VertexConsumerProvider bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if (animatable.isSitting()) {
                    return;
                }

                // Traslación del objeto
                poseStack.translate(bone.getPosX(), -bone.getPosY() - 0.20F, bone.getPosZ() - 0.15F);

                // Rotación del objeto
                poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(bone.getRotX() - 40.5F)); // Ok (-40.5)

                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

    @Override
    public @NotNull Identifier getTextureLocation(@NotNull BrotecitoEntity pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(BrotecitoEntity pEntity, float pEntityYaw, float pPartialTicks, @NotNull MatrixStack pMatrixStack,
                       @NotNull VertexConsumerProvider pBuffer, int pPackedLight) {

        if (pEntity.isBaby()) {
            pMatrixStack.scale(0.3f, 0.3f, 0.3f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
