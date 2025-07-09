package com.crawkatt.meicamod;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.client.*;
import com.crawkatt.meicamod.particle.ModParticles;
import com.crawkatt.meicamod.particle.custom.KappaPrideParticles;
import com.crawkatt.meicamod.util.ModModelPredicateProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.*;

public class MeicaModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BROTECITO_SPROUT, RenderLayer.getCutout());

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 0xBFEA75, ModBlocks.BROTECITO_SPROUT);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.MEICA_LAYER, MeicaModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntities.MEICA, MeicaRenderer::new);

        EntityRendererRegistry.register(ModEntities.BROTECITO_MAMADO, BrotecitoMamadoRenderer::new);
        EntityRendererRegistry.register(ModEntities.BROTECITO, BrotecitoRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.KAPPA_PRIDE_PARTICLES, KappaPrideParticles.Factory::new);

        ModModelPredicateProvider.registerModModels();
    }
}
