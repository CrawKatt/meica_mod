package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.brotecito.BrotecitoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BrotecitoModel extends GeoModel<BrotecitoEntity> {
	@Override
	public ResourceLocation getModelResource(BrotecitoEntity brotecitoEntity) {
		return new ResourceLocation(MeicaMod.MODID, "geo/brotecito.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BrotecitoEntity brotecitoEntity) {
		return new ResourceLocation(MeicaMod.MODID, "textures/entity/brotecito.png");
	}

	@Override
	public ResourceLocation getAnimationResource(BrotecitoEntity brotecitoEntity) {
		return null;
	}
}