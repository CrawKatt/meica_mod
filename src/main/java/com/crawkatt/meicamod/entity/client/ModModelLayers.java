package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer MEICA_LAYER =
            new EntityModelLayer(new Identifier(MeicaMod.MOD_ID, "meica"), "main");
}
