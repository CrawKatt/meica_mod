package com.crawkatt.meicamod;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.block.entity.ModBlockEntities;
import com.crawkatt.meicamod.block.entity.renderer.BrotenitaMelterBlockEntityRenderer;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.client.*;
import com.crawkatt.meicamod.event.FogHandler;
import com.crawkatt.meicamod.event.FogModifyCallback;
import com.crawkatt.meicamod.event.SpellWheelOverlay;
import com.crawkatt.meicamod.event.UseItemEvent;
import com.crawkatt.meicamod.keybind.ModKeyBindings;
import com.crawkatt.meicamod.networking.ModMessages;
import com.crawkatt.meicamod.particle.ModParticles;
import com.crawkatt.meicamod.particle.custom.KappaPrideParticles;
import com.crawkatt.meicamod.screen.BrotecitoScreen;
import com.crawkatt.meicamod.screen.BrotenitaMelterScreen;
import com.crawkatt.meicamod.screen.ModScreenHandlers;
import com.crawkatt.meicamod.util.ModModelPredicateProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class MeicaModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModKeyBindings.register();
        ClientTickEvents.END_CLIENT_TICK.register(new UseItemEvent());
        HudRenderCallback.EVENT.register(new SpellWheelOverlay());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ModKeyBindings.OPEN_SPELL_WHEEL.wasPressed()) {
                SpellWheelOverlay.open();
            }
        });

        // Render para la Brotenita (Necesario para que el bloque no tenga fondos negros)
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BROTENITA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BROTENITA_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BROTECITO_SPROUT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEICA_TEDDY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ZERO_FIVE_TEDDY, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BROTENITA_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BROTENITA_TRAPDOOR, RenderLayer.getCutout());

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 0xBFEA75, ModBlocks.BROTECITO_SPROUT);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.MEICA_LAYER, MeicaModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntities.MEICA, MeicaRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.ZERO_FIVE_LAYER, ZeroFiveModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntities.ZERO_FIVE, ZeroFiveRenderer::new);

        EntityRendererRegistry.register(ModEntities.BROTECITO_MAMADO, BrotecitoMamadoRenderer::new);
        EntityRendererRegistry.register(ModEntities.BROTECITO, BrotecitoRenderer::new);

        EntityRendererRegistry.register(ModEntities.PLAYER_CLONE, PlayerCloneRenderer::new);
        EntityRendererRegistry.register(ModEntities.MAGIC_CIRCLE, MagicCircleRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.KAPPA_PRIDE_PARTICLES, KappaPrideParticles.Factory::new);

        ModModelPredicateProvider.registerModModels();

        HandledScreens.register(ModScreenHandlers.BROTENITA_MELTER_SCREEN_HANDLER, BrotenitaMelterScreen::new);
        HandledScreens.register(ModScreenHandlers.BROTECITO_SCREEN_HANDLER, BrotecitoScreen::new);

        ModMessages.registerS2CPackets();

        BlockEntityRendererFactories.register(ModBlockEntities.BROTENITA_MELTER_BE, BrotenitaMelterBlockEntityRenderer::new);

        FogModifyCallback.EVENT.register(new FogHandler());
    }
}
