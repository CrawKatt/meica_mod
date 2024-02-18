package com.example.examplemod.entity.client.renderer;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.client.model.BrotecitoModel;
import com.example.examplemod.entity.custom.BrotecitoEntity;
import com.example.examplemod.entity.variant.BrotecitoVariant;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class BrotecitoRenderer extends GeoEntityRenderer<BrotecitoEntity> {

    // Define la ubicación de las texturas de las variantes del Brotecito
    public static final Map<BrotecitoVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BrotecitoVariant.class), (p_114874_) -> {
                p_114874_.put(BrotecitoVariant.DEFAULT,
                        new ResourceLocation(ExampleMod.MODID, "textures/entity/brotecito/brotecito.png"));
                p_114874_.put(BrotecitoVariant.DARK,
                        new ResourceLocation(ExampleMod.MODID, "textures/entity/brotecito/dark_brotecito.png"));
            });

    public BrotecitoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BrotecitoModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(BrotecitoEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public RenderType getRenderType(BrotecitoEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Define el tamaño del modelo del Brotecito en el juego
        if(animatable.isBaby()) {
            stack.scale(0.65F, 0.65F, 0.65F);
        } else {
            stack.scale(1.3F, 1.3F, 1.3F);
        }

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
