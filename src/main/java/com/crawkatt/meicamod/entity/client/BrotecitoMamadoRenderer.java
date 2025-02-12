package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.BrotecitoMamadoEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BrotecitoMamadoRenderer extends GeoEntityRenderer<BrotecitoMamadoEntity> {
    public static final Identifier TEXTURE = new Identifier(MeicaMod.MOD_ID, "textures/entity/brotecito_mamado.png");

    public BrotecitoMamadoRenderer(EntityRendererFactory.Context pContext) {
        super(pContext, new BrotecitoMamadoModel());
        this.shadowRadius = 0.6f; // Tamaño de la sombra de la Entidad
    }

    @Override
    public @NotNull Identifier getTextureLocation(@NotNull BrotecitoMamadoEntity pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(BrotecitoMamadoEntity pEntity, float pEntityYaw, float pPartialTicks, @NotNull MatrixStack pMatrixStack,
                       @NotNull VertexConsumerProvider pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
