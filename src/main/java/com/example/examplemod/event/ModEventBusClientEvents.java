package com.example.examplemod.event;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.client.BrotecitoModel;
import com.example.examplemod.entity.client.MeicaModel;
import com.example.examplemod.entity.client.ModModelLayers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.BROTECITO_LAYER, BrotecitoModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MEICA_LAYER, MeicaModel::createBodyLayer);
    }
}
