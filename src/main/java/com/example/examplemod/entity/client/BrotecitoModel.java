package com.example.examplemod.entity.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.brotecito.BrotecitoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BrotecitoModel extends GeoModel<BrotecitoEntity> {
	@Override
	public ResourceLocation getModelResource(BrotecitoEntity brotecitoEntity) {
		return new ResourceLocation(ExampleMod.MODID, "geo/brotecito.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BrotecitoEntity brotecitoEntity) {
		return new ResourceLocation(ExampleMod.MODID, "textures/entity/brotecito.png");
	}

	@Override
	public ResourceLocation getAnimationResource(BrotecitoEntity brotecitoEntity) {
		return null;
	}
}