package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.BrotecitoEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;

import java.util.Objects;

public class BrotecitoRenderer extends GeoEntityRenderer<BrotecitoEntity> {
    public static final Identifier TEXTURE = new Identifier(MeicaMod.MOD_ID, "textures/entity/brotecito.png");

    protected ItemStack mainHandItem;
    protected ItemStack offHandItem;

    public BrotecitoRenderer(EntityRendererFactory.Context pContext) {
        super(pContext, new BrotecitoModel());
        this.shadowRadius = 0.6f; // Tamaño de la sombra de la Entidad

        this.addRenderLayer(new BlockAndItemGeoLayer<>(this, (bone, animatable) -> {
            if (Objects.equals(bone.getName(), "ir_rightArm")) {
                return animatable.getStackInHand(Hand.MAIN_HAND);
            } else if (Objects.equals(bone.getName(), "il_leftArm")) {
                return animatable.getStackInHand(Hand.OFF_HAND);
            }
            return null;
        }, (bone, animatable) -> null) {
            @Override
            protected ModelTransformationMode getTransformTypeForStack(GeoBone bone, ItemStack stack, BrotecitoEntity animatable) {
                if (Objects.equals(bone.getName(), "il_leftArm")) {
                    return ModelTransformationMode.THIRD_PERSON_LEFT_HAND;
                }
                return ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
            }

            @Override
            protected void renderStackForBone(MatrixStack poseStack, GeoBone bone, ItemStack stack, BrotecitoEntity animatable, VertexConsumerProvider bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if (animatable.isSitting()) {
                    return;
                }

                if (stack == BrotecitoRenderer.this.mainHandItem) {
                    // Traslación del objeto
                    poseStack.translate(bone.getPosX(), -bone.getPosY() - 0.20F, bone.getPosZ() - 0.15F);

                    // Rotación del objeto
                    poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(bone.getRotX() - 40.5F)); // Ok (-40.5)
                } else if (stack == BrotecitoRenderer.this.offHandItem) {
                    poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90f));

                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0, bone.getPosY() - 0.20F, bone.getPosZ() + 1.15F);
                        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                    }
                }

                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });

        this.addRenderLayer(new ItemArmorGeoLayer<>(this){
            @Override
            protected ItemStack getArmorItemForBone(GeoBone bone, BrotecitoEntity animatable) {
                return super.getArmorItemForBone(bone, animatable);
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

    @Override
    public void preRender(MatrixStack poseStack, BrotecitoEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        this.mainHandItem = animatable.getMainHandStack();
        this.offHandItem = animatable.getOffHandStack();
    }
}
