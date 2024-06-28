package com.example.examplemod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MeicaModel<T extends LivingEntity> extends HierarchicalModel<T> implements ArmedModel {
	private final ModelPart head;
	private final ModelPart headwear;
	private final ModelPart body;
	private final ModelPart jacket;
	private final ModelPart left_arm;
	private final ModelPart left_sleve;
	private final ModelPart right_arm;
	private final ModelPart right_sleve;
	private final ModelPart left_leg;
	private final ModelPart left_pants;
	private final ModelPart right_leg;
	private final ModelPart right_pants;

	public MeicaModel(ModelPart root) {
		this.head = root.getChild("head");
		this.headwear = root.getChild("headwear");
		this.body = root.getChild("body");
		this.jacket = root.getChild("jacket");
		this.left_arm = root.getChild("left_arm");
		this.left_sleve = root.getChild("left_sleve");
		this.right_arm = root.getChild("right_arm");
		this.right_sleve = root.getChild("right_sleve");
		this.left_leg = root.getChild("left_leg");
		this.left_pants = root.getChild("left_pants");
		this.right_leg = root.getChild("right_leg");
		this.right_pants = root.getChild("right_pants");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition headwear = partdefinition.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition jacket = partdefinition.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(9.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
		PartDefinition left_sleve = partdefinition.addOrReplaceChild("left_sleve", CubeListBuilder.create().texOffs(48, 48).addBox(9.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-13.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));
		PartDefinition right_sleve = partdefinition.addOrReplaceChild("right_sleve", CubeListBuilder.create().texOffs(40, 32).addBox(-13.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));
		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));
		PartDefinition left_pants = partdefinition.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-2.0F, 12.0F, 0.0F));
		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 12.0F, 0.0F));
		PartDefinition right_pants = partdefinition.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(2.0F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	// Animaciones de Meica
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = headPitch * ((float)Math.PI / 180F);
		this.headwear.yRot = this.head.yRot;
		this.headwear.xRot = this.head.xRot;
		this.right_arm.xRot = -1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
		this.right_sleve.xRot = -1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
		this.left_arm.xRot = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
		this.left_sleve.xRot = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
		this.right_leg.xRot = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
		this.right_pants.xRot = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
		this.left_leg.xRot = -1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
		this.left_pants.xRot = -1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;

		// Animación de apuntar con el arco
		ItemStack itemstack = entity.getMainHandItem();
		if (itemstack.getItem() == Items.BOW && entity.isUsingItem()) {
			this.right_arm.yRot = -0.1F + this.head.yRot;
			this.right_arm.xRot = -((float)Math.PI / 2F) + this.head.xRot;
			this.right_sleve.yRot = -0.1F + this.head.yRot;
			this.right_sleve.xRot = -((float)Math.PI / 2F) + this.head.xRot;
			this.left_arm.yRot = 0.1F + this.head.yRot;
			this.left_arm.xRot = -((float)Math.PI / 2F) + this.head.xRot;
			this.left_sleve.yRot = 0.1F + this.head.yRot;
			this.left_sleve.xRot = -((float)Math.PI / 2F) + this.head.xRot;
		} else {
			this.left_arm.yRot = 0.0F;
			this.left_arm.xRot = 0.0F;
			this.left_sleve.yRot = 0.0F;
			this.left_sleve.xRot = 0.0F;
			this.right_arm.yRot = 0.0F;
			this.right_arm.xRot = 0.0F;
			this.right_sleve.yRot = 0.0F;
			this.right_sleve.xRot = 0.0F;
		}
	}

	private float triangleWave(float p_228293_1_, float p_228293_2_) {
		return (Math.abs(p_228293_1_ % p_228293_2_ - p_228293_2_ * 0.5F) - p_228293_2_ * 0.25F) / (p_228293_2_ * 0.25F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight,
							   int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		headwear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		jacket.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_sleve.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_sleve.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_pants.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_pants.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return body;
	}

	@Override
	public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
		this.right_arm.translateAndRotate(poseStack);
	}
}