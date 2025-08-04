package com.crawkatt.meicamod.block.client;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.entity.MeicaStatueBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class MeicaStatueBlockModel extends GeoModel<MeicaStatueBlockEntity> {
    @Override
    public Identifier getModelResource(MeicaStatueBlockEntity animatable) {
        return new Identifier(MeicaMod.MOD_ID, "geo/meica_statue.geo.json");
    }

    @Override
    public Identifier getTextureResource(MeicaStatueBlockEntity animatable) {
        return new Identifier(MeicaMod.MOD_ID, "textures/block/meica_statue.png");
    }

    @Override
    public Identifier getAnimationResource(MeicaStatueBlockEntity animatable) {
        return null;
    }
}
