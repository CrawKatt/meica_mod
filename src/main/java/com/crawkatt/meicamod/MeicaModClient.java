package com.crawkatt.meicamod;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.block.entity.ModBlockEntities;
import com.crawkatt.meicamod.block.entity.renderer.BrotenitaMelterBlockEntityRenderer;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.client.*;
import com.crawkatt.meicamod.networking.ModMessages;
import com.crawkatt.meicamod.particle.ModParticles;
import com.crawkatt.meicamod.particle.custom.KappaPrideParticles;
import com.crawkatt.meicamod.screen.BrotenitaMelterScreen;
import com.crawkatt.meicamod.screen.ModScreenHandlers;
import com.crawkatt.meicamod.util.ModModelPredicateProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class MeicaModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Render para la Brotenita (Necesario para que el bloque no tenga fondos negros)
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BROTENITA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BROTENITA_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BROTENITA_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BROTENITA_TRAPDOOR, RenderLayer.getCutout());

        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.MEICA_LAYER, MeicaModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntities.MEICA, MeicaRenderer::new);

        EntityRendererRegistry.register(ModEntities.BROTECITO_MAMADO, BrotecitoMamadoRenderer::new);
        EntityRendererRegistry.register(ModEntities.BROTECITO, BrotecitoRenderer::new);

        EntityRendererRegistry.register(ModEntities.PLAYER_CLONE, PlayerCloneRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.KAPPA_PRIDE_PARTICLES, KappaPrideParticles.Factory::new);

        ModModelPredicateProvider.registerModModels();

        HandledScreens.register(ModScreenHandlers.BROTENITA_MELTER_SCREEN_HANDLER, BrotenitaMelterScreen::new);

        ModMessages.registerS2CPackets();

        BlockEntityRendererFactories.register(ModBlockEntities.BROTENITA_MELTER_BE, BrotenitaMelterBlockEntityRenderer::new);
    }
}
