package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.BrotecitoEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class BrotecitoModel extends GeoModel<BrotecitoEntity> {
    @Override
    public Identifier getModelResource(BrotecitoEntity animatable) {
        return new Identifier(MeicaMod.MOD_ID, "geo/brotecito.geo.json");
    }

    @Override
    public Identifier getTextureResource(BrotecitoEntity animatable) {
        return new Identifier(MeicaMod.MOD_ID, "textures/entity/brotecito.png");
    }

    @Override
    public Identifier getAnimationResource(BrotecitoEntity animatable) {
        return new Identifier(MeicaMod.MOD_ID, "animations/brotecito.animation.json");
    }
}
