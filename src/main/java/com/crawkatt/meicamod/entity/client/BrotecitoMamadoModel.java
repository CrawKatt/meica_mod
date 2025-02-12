package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.BrotecitoMamadoEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class BrotecitoMamadoModel extends GeoModel<BrotecitoMamadoEntity> {
    @Override
    public Identifier getModelResource(BrotecitoMamadoEntity brotecitoMamadoEntity) {
        return new Identifier(MeicaMod.MOD_ID, "geo/brotecito_mamado.geo.json");
    }

    @Override
    public Identifier getTextureResource(BrotecitoMamadoEntity brotecitoMamadoEntity) {
        return new Identifier(MeicaMod.MOD_ID, "textures/entity/brotecito_mamado.png");
    }

    @Override
    public Identifier getAnimationResource(BrotecitoMamadoEntity brotecitoMamadoEntity) {
        return new Identifier(MeicaMod.MOD_ID, "animations/brotecito_mamado.animation.json");
    }
}
