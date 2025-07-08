package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.MagicCircleEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class MagicCircleRenderer extends EntityRenderer<MagicCircleEntity> {
    private static final Identifier TEXTURE = new Identifier(MeicaMod.MOD_ID, "textures/entity/magic_circle.png");
    public MagicCircleRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public boolean shouldRender(MagicCircleEntity entity, Frustum frustum, double x, double y, double z) {
        return super.shouldRender(entity, frustum, x, y, z);
    }

    @Override
    public void render(MagicCircleEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        renderCircle(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private void renderCircle(MagicCircleEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                              VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(0.0D, 0.0D, 0.0D);
        matrices.multiply(this.dispatcher.getRotation());

        float maxScale = 1.5f;
        float animationDurationTicks =  5.0f;
        float animationProgress = Math.min((entity.age + tickDelta) / animationDurationTicks, 1.0f);
        float currentScale = maxScale * animationProgress;

        float animationTime = entity.age + tickDelta;
        float degressPerSecond = 180.0f;
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(animationTime * degressPerSecond / 20));

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

    @Override
    public Identifier getTexture(MagicCircleEntity entity) {
        return TEXTURE;
    }
}
