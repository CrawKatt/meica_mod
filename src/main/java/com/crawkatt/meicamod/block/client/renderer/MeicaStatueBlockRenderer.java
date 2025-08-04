package com.crawkatt.meicamod.block.client.renderer;

import com.crawkatt.meicamod.block.client.MeicaStatueBlockModel;
import com.crawkatt.meicamod.block.entity.MeicaStatueBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MeicaStatueBlockRenderer extends GeoBlockRenderer<MeicaStatueBlockEntity> {
    public MeicaStatueBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(new MeicaStatueBlockModel());
    }

    @Override
    public RenderLayer getRenderType(MeicaStatueBlockEntity animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityCutout(getTextureLocation(animatable));
    }
}
