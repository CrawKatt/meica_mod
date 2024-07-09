package com.example.examplemod.entity.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.meica.MeicaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MeicaRenderer extends MobRenderer<MeicaEntity, MeicaModel<MeicaEntity>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/meica.png");
    public MeicaRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new MeicaModel<>(pContext.bakeLayer(ModModelLayers.MEICA_LAYER)), 0.6f); // Tamaño de la sombra de la Entidad
        this.addLayer(new ItemInHandLayer<>(this, pContext.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull MeicaEntity pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(MeicaEntity pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pMatrixStack,
                       @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
