package com.example.examplemod.entity.client.model;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.client.renderer.BrotecitoRenderer;
import com.example.examplemod.entity.custom.BrotecitoEntity;
import com.example.examplemod.entity.variant.BrotecitoVariant;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.Map;

public class BrotecitoModel extends AnimatedGeoModel<BrotecitoEntity> {

    public static final Map<BrotecitoVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BrotecitoVariant.class), (p_114874_) -> {
                p_114874_.put(BrotecitoVariant.DEFAULT,
                        new ResourceLocation(ExampleMod.MODID, "geo/brotecito.geo.json"));
                p_114874_.put(BrotecitoVariant.DARK,
                        new ResourceLocation(ExampleMod.MODID, "geo/dark_brotecito.geo.json"));
            });

    @Override
    public ResourceLocation getModelLocation(BrotecitoEntity object) {
        //return new ResourceLocation(ExampleMod.MODID, "geo/brotecito.geo.json");
        return LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getTextureLocation(BrotecitoEntity object) {
        return BrotecitoRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    // Define el comportamiento de mirar al jugador que lo haya tameado
    @Override
    public void setLivingAnimations(BrotecitoEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(BrotecitoEntity animatable) {
        return new ResourceLocation(ExampleMod.MODID, "animations/brotecito.animation.json");
    }
}
