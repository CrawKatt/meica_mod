package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class MagicCircleFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private static final Identifier TEXTURE = new Identifier(MeicaMod.MOD_ID, "textures/entity/magic_circle.png");

    public MagicCircleFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity,
                       float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {

        if (!entity.isUsingItem()) return;

        matrices.push();

        float verticalPosition = entity.getHeight() * 0.02f;
        matrices.translate(0.0D, -verticalPosition, 0.0D);

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-headYaw));

        float horizontalDistance = -1.2f;
        matrices.translate(0.0D, 0.0D, horizontalDistance);

        int useTime = entity.getItemUseTime();
        float animationTime = useTime + tickDelta;
        float degreesPerSecond = 180.0f;
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(animationTime * degreesPerSecond / 20));

        float maxScale = 1.5f;
        float animationDurationTicks = 5.0f;
        float animationProgress = Math.min(animationTime / animationDurationTicks, 1.0f);
        float currentScale = maxScale * animationProgress;
        matrices.scale(-currentScale, -currentScale, currentScale);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));

        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), -0.5F, -0.5F, 0.0F)
                .color(255, 255, 255, 255)
                .texture(0.0F, 1.0F)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0, 1, 0)
                .next();

        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), 0.5F, -0.5F, 0.0F)
                .color(255, 255, 255, 255)
                .texture(1.0F, 1.0F)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0, 1, 0)
                .next();

        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), 0.5F, 0.5F, 0.0F)
                .color(255, 255, 255, 255)
                .texture(1.0F, 0.0F)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0, 1, 0)
                .next();

        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), -0.5F, 0.5F, 0.0F)
                .color(255, 255, 255, 255)
                .texture(0.0F, 0.0F)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(0, 1, 0)
                .next();

        matrices.pop();
    }
}