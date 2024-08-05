package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.brotecito.BrotecitoMamadoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BrotecitoMamadoModel extends GeoModel<BrotecitoMamadoEntity> {
    @Override
    public ResourceLocation getModelResource(BrotecitoMamadoEntity brotecitoMamadoEntity) {
        return new ResourceLocation(MeicaMod.MODID, "geo/brotecito_mamado.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BrotecitoMamadoEntity brotecitoMamadoEntity) {
        return new ResourceLocation(MeicaMod.MODID, "textures/entity/brotecito_mamado.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BrotecitoMamadoEntity brotecitoMamadoEntity) {
        return new ResourceLocation(MeicaMod.MODID, "animations/brotecito_mamado.animation.json");
    }

}
