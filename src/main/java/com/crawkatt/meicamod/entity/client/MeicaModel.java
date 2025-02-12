package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.entity.custom.MeicaEntity;
import com.crawkatt.meicamod.item.ModItems;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

public class MeicaModel<T extends MeicaEntity> extends BipedEntityModel<T> {
    public final ModelPart leftSleeve;
    public final ModelPart rightSleeve;
    public final ModelPart leftPants;
    public final ModelPart rightPants;
    public final ModelPart jacket;

    public MeicaModel(ModelPart modelPart) {
        super(modelPart, RenderLayer::getEntityTranslucent);
        this.leftSleeve = modelPart.getChild("left_sleeve");
        this.rightSleeve = modelPart.getChild("right_sleeve");
        this.leftPants = modelPart.getChild("left_pants");
        this.rightPants = modelPart.getChild("right_pants");
        this.jacket = modelPart.getChild("jacket");
    }

    // Uso de modelo Slim por defecto
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
    public void animateModel(T entity, float limbAngle, float limbDistance, float tickDelta) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;

        ItemStack mainHandItem = entity.getMainHandStack();
        if (mainHandItem.isOf(ModItems.MEICA_BOW)) { // Reemplaza con tu ítem de arco
            if (entity.isAttacking()) {
                if (entity.getMainArm() == Arm.RIGHT) {
                    this.rightArmPose = ArmPose.BOW_AND_ARROW;
                } else {
                    this.leftArmPose = ArmPose.BOW_AND_ARROW;
                }
            }
        }

        super.animateModel(entity, limbAngle, limbDistance, tickDelta);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        // Animación de ataque con arco
        ItemStack mainHandItem = entity.getMainHandStack();
        if (entity.isAttacking() && (mainHandItem.isEmpty() || !mainHandItem.isOf(ModItems.MEICA_BOW))) {
            float swingProgress = MathHelper.sin(this.handSwingProgress * MathHelper.PI);
            float swingAmount = MathHelper.sin((1.0F - (1.0F - this.handSwingProgress) * (1.0F - this.handSwingProgress)) * MathHelper.PI);

            this.rightArm.roll = 0.0F;
            this.leftArm.roll = 0.0F;

            this.rightArm.yaw = -(0.1F - swingProgress * 0.6F);
            this.leftArm.yaw = 0.1F - swingProgress * 0.6F;

            this.rightArm.pitch = -1.5707964F;
            this.leftArm.pitch = -1.5707964F;

            this.rightArm.pitch -= swingProgress * 1.2F - swingAmount * 0.4F;
            this.leftArm.pitch -= swingProgress * 1.2F - swingAmount * 0.4F;

            CrossbowPosing.swingArms(this.rightArm, this.leftArm, animationProgress);
        }

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