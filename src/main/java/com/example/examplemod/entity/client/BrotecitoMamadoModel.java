package com.example.examplemod.entity.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.brotecito.BrotecitoMamadoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BrotecitoMamadoModel extends GeoModel<BrotecitoMamadoEntity> {
    @Override
    public ResourceLocation getModelResource(BrotecitoMamadoEntity brotecitoMamadoEntity) {
        return new ResourceLocation(ExampleMod.MODID, "geo/brotecito_mamado.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BrotecitoMamadoEntity brotecitoMamadoEntity) {
        return new ResourceLocation(ExampleMod.MODID, "textures/entity/brotecito_mamado.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BrotecitoMamadoEntity brotecitoMamadoEntity) {
        return new ResourceLocation(ExampleMod.MODID, "animations/brotecito_mamado.animation.json");
    }

}
