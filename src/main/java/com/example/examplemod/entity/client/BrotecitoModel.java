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

public class BrotecitoModel<T extends LivingEntity> extends HierarchicalModel<T> implements ArmedModel {
	private final ModelPart bone;
	private final ModelPart bone2;

	public BrotecitoModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.bone2 = root.getChild("bone2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(15, 39).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(1, 8).addBox(-5.0F, -9.0F, 5.0F, 10.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(26, 28).addBox(-5.0F, -10.0F, -4.0F, 10.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(34, 19).addBox(-4.0F, -11.0F, -3.0F, 8.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(15, 1).addBox(-2.0F, -6.0F, -3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(24, 1).addBox(-4.0F, -3.0F, -2.0F, 8.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(1, 35).addBox(-6.0F, -9.0F, -4.0F, 2.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(1, 19).addBox(3.0F, -9.0F, -4.0F, 3.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 5).addBox(0.0F, -12.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(33, 62).addBox(-7.0F, -2.0F, 6.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(33, 56).addBox(-7.0F, -3.0F, -8.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 62).addBox(-8.0F, -3.0F, 7.0F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(1, 56).addBox(-6.0F, -2.0F, 6.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(18, 9).addBox(4.0F, -9.0F, -5.0F, 1.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(33, 59).addBox(-7.0F, -2.0F, 6.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(29, 53).addBox(-8.0F, -3.0F, -8.0F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 59).addBox(-8.0F, -3.0F, 7.0F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(1, 53).addBox(-6.0F, -2.0F, 6.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 1).addBox(0.0F, -6.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(5, 1).addBox(0.0F, -6.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition leafLower_r1 = bone.addOrReplaceChild("leafLower_r1", CubeListBuilder.create().texOffs(47, 9).addBox(-2.3827F, -0.2609F, 1.5675F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(56, 9).addBox(-2.3827F, -7.2609F, 1.5675F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(51, 1).addBox(-3.3827F, -6.2609F, 1.5675F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9651F, -11.2483F, -5.131F, 0.7854F, 0.3927F, 0.0F));

		PartDefinition leaftRoot2_r1 = bone.addOrReplaceChild("leaftRoot2_r1", CubeListBuilder.create().texOffs(10, 1).addBox(-0.8827F, -1.9942F, 1.1189F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7119F, -10.2706F, -3.4122F, 0.3927F, 0.3927F, 0.0F));

		PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition arm_r1 = bone2.addOrReplaceChild("arm_r1", CubeListBuilder.create().texOffs(51, 12).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -5.0767F, -6.9556F, -0.6545F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bone2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return bone;
	}

	public ModelPart getArmRight() {
		return this.bone2.getChild("arm_r1");
	}

	@Override
	public void translateToHand(HumanoidArm p_102108_, PoseStack p_102109_) {
		this.getArmRight().translateAndRotate(p_102109_);
	}

	@Override
	public void setupAnim(T p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {

	}
}