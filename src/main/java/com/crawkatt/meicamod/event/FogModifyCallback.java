package com.crawkatt.meicamod.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;

@Environment(EnvType.CLIENT)
public interface FogModifyCallback {
    Event<FogModifyCallback> EVENT = EventFactory.createArrayBacked(FogModifyCallback.class,
            (listeners) -> (camera, fogType, viewDistance, thickFog, tickDelta) -> {
                for (FogModifyCallback listener : listeners) {
                    listener.modifyFog(camera, fogType, viewDistance, thickFog, tickDelta);
                }
            });

    void modifyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta);
}
