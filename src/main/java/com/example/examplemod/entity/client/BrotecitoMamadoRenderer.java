package com.example.examplemod.entity.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.brotecito.BrotecitoMamadoEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BrotecitoMamadoRenderer extends GeoEntityRenderer<BrotecitoMamadoEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/brotecito_mamado.png");
    public BrotecitoMamadoRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BrotecitoMamadoModel());
        this.shadowRadius = 0.6f; // Tamaño de la sombra de la Entidad
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BrotecitoMamadoEntity pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(BrotecitoMamadoEntity pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pMatrixStack,
                       @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
