package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.entity.custom.ZeroFiveEntity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class ZeroFiveModel<T extends ZeroFiveEntity> extends BipedEntityModel<T> {
    public final ModelPart leftSleeve;
    public final ModelPart rightSleeve;
    public final ModelPart leftPants;
    public final ModelPart rightPants;
    public final ModelPart jacket;

    public ZeroFiveModel(ModelPart modelPart) {
        super(modelPart, RenderLayer::getEntityTranslucent);
        this.leftSleeve = modelPart.getChild("left_sleeve");
        this.rightSleeve = modelPart.getChild("right_sleeve");
        this.leftPants = modelPart.getChild("left_pants");
        this.rightPants = modelPart.getChild("right_pants");
        this.jacket = modelPart.getChild("jacket");
    }

    public static TexturedModelData createBodyLayer() {
        Dilation deformation = new Dilation(0.0F);
        ModelData meshDefinition = BipedEntityModel.getModelData(Dilation.NONE, 0.0F);
        ModelPartData partdefinition = meshDefinition.getRoot();

        partdefinition.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation), ModelTransform.pivot(5.0F, 2.5F, 0.0F));
        partdefinition.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation), ModelTransform.pivot(-5.0F, 2.5F, 0.0F));
        partdefinition.addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation.add(0.25F)), ModelTransform.pivot(5.0F, 2.5F, 0.0F));
        partdefinition.addChild("right_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation.add(0.25F)), ModelTransform.pivot(-5.0F, 2.5F, 0.0F));

        partdefinition.addChild("left_leg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
        partdefinition.addChild("left_pants", ModelPartBuilder.create().uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.add(0.25F)), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
        partdefinition.addChild("right_pants", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.add(0.25F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
        partdefinition.addChild("jacket", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation.add(0.25F)), ModelTransform.NONE);

        return TexturedModelData.of(meshDefinition, 64, 64);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return Iterables.concat(super.getBodyParts(), ImmutableList.of(
                this.leftPants,
                this.rightPants,
                this.leftSleeve,
                this.rightSleeve,
                this.jacket
        ));
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        // Actualizar partes del modelo
        this.leftPants.copyTransform(this.leftLeg);
        this.rightPants.copyTransform(this.rightLeg);
        this.leftSleeve.copyTransform(this.leftArm);
        this.rightSleeve.copyTransform(this.rightArm);
        this.jacket.copyTransform(this.body);
    }

    @Override
    public void setVisible(boolean allVisible) {
        super.setVisible(allVisible);
        this.leftSleeve.visible = allVisible;
        this.rightSleeve.visible = allVisible;
        this.leftPants.visible = allVisible;
        this.rightPants.visible = allVisible;
        this.jacket.visible = allVisible;
    }
}