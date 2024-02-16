package com.example.examplemod.entity.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.MeicaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MeicaRenderer extends GeoEntityRenderer<MeicaEntity> {
    public MeicaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MeicaModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(MeicaEntity instance) {
        return new ResourceLocation(ExampleMod.MODID, "textures/entity/meica/meica.png");
    }

    @Override
    public RenderType getRenderType(
            MeicaEntity animatable,
            float partialTicks,
            PoseStack stack,
            MultiBufferSource renderTypeBuffer,
            VertexConsumer vertexBuilder,
            int packedLightIn,
            ResourceLocation textureLocation
    ) {
        // Define el tamaño del modelo del Brotecito en el juego
        if(animatable.isBaby()) {
            stack.scale(0.65F, 0.65F, 0.65F);
        } else {
            stack.scale(1.3F, 1.3F, 1.3F);
        }

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }

    @Override
    public void render(
            MeicaEntity entity,
            float entityYaw,
            float partialTicks,
            PoseStack stack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);

        ItemStack itemStack = entity.getMainHandItem();
        if (!itemStack.isEmpty()) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            stack.pushPose();

            stack.translate(0.3, 1.8, 0.8); // Posición del objeto
            stack.mulPose(Vector3f.YP.rotationDegrees(180));
            stack.mulPose(Vector3f.ZP.rotationDegrees(15));
            stack.scale(0.8F, 0.8F, 0.8F);

            itemRenderer.renderStatic(
                    entity,
                    itemStack,
                    ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
                    false,
                    stack,
                    buffer,
                    entity.level,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    1
            );

            stack.popPose();
        }
    }
}
