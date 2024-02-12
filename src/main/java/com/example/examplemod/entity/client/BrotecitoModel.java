package com.example.examplemod.entity.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.BrotecitoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BrotecitoModel extends AnimatedGeoModel<BrotecitoEntity> {
    @Override
    public ResourceLocation getModelLocation(BrotecitoEntity brotecitoEntity) {
        return new ResourceLocation(ExampleMod.MODID, "geo/brotecito.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BrotecitoEntity brotecitoEntity) {
        return new ResourceLocation(ExampleMod.MODID, "textures/entity/brotecito/brotecito.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(BrotecitoEntity brotecitoEntity) {
        return new ResourceLocation(ExampleMod.MODID, "animations/brotecito.animation.json");
    }
}
