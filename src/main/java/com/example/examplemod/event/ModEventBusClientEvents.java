package com.example.examplemod.event;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.ModEntities;
import com.example.examplemod.entity.client.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.MEICA.get(), MeicaRenderer::new);
        event.registerEntityRenderer(ModEntities.BROTECITO.get(), BrotecitoRenderer::new);
        event.registerEntityRenderer(ModEntities.BROTECITO_MAMADO.get(), BrotecitoMamadoRenderer::new);
        event.registerEntityRenderer(ModEntities.PLAYER_CLONE.get(), PlayerCloneRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MeicaModel.LAYER_LOCATION, MeicaModel::createBodyLayer);
    }
}
