package com.example.examplemod.entity.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.MeicaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class MeicaModel extends AnimatedGeoModel<MeicaEntity> {

    @Override
    public ResourceLocation getModelLocation(MeicaEntity object) {
        return new ResourceLocation(ExampleMod.MODID, "geo/meica.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(MeicaEntity object) {
        return new ResourceLocation(ExampleMod.MODID, "textures/entity/meica/meica.png");
        //return MeicaRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    // Define el comportamiento de mirar al jugador que lo haya tameado
    @Override
    public void setLivingAnimations(MeicaEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MeicaEntity animatable) {
        return new ResourceLocation(ExampleMod.MODID, "animations/meica.animation.json");
    }
}
