package com.example.examplemod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public class BrotecitoModel<T extends LivingEntity> extends HierarchicalModel<T> implements ArmedModel {
	private final ModelPart root;
	public final ModelPart armRight;

	public BrotecitoModel(ModelPart root) {
		this.root = root.getChild("bone");
		ModelPart bone2 = root.getChild("bone2");
		this.armRight = bone2.getChild("arm_r1");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(16, 35).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(1, 9).addBox(-5.0F, -9.0F, 5.0F, 10.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(26, 21).addBox(-5.0F, -10.0F, -4.0F, 10.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(34, 13).addBox(-4.0F, -11.0F, -3.0F, 8.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(5, 24).addBox(-2.0F, -6.0F, -3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(21, -2).addBox(-4.0F, -3.0F, -2.0F, 8.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(-6, 18).addBox(-6.0F, -9.0F, -4.0F, 2.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(2, 17).addBox(3.0F, -9.0F, -4.0F, 3.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 3).addBox(0.0F, -12.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(34, 57).addBox(-7.0F, -2.0F, 6.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(1, 52).addBox(-7.0F, -3.0F, -8.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 58).addBox(-8.0F, -3.0F, 7.0F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(36, 61).addBox(-6.0F, -2.0F, 6.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(12, 16).addBox(4.0F, -9.0F, -5.0F, 1.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(34, 53).addBox(-7.0F, -2.0F, 6.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 49).addBox(-8.0F, -3.0F, -8.0F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 55).addBox(-8.0F, -3.0F, 7.0F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(2, 61).addBox(-6.0F, -2.0F, 6.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(0.0F, -6.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(4, 0).addBox(0.0F, -6.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(56, 8).addBox(-2.3827F, -0.2609F, 1.5675F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(60, 8).addBox(-2.3827F, -7.2609F, 1.5675F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(52, 0).addBox(-3.3827F, -6.2609F, 1.5675F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9651F, -11.2483F, -5.131F, 0.7854F, 0.3927F, 0.0F));

		PartDefinition cube_r5 = bone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(9, 0).addBox(-0.8827F, -1.9942F, 1.1189F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7119F, -10.2706F, -3.4122F, 0.3927F, 0.3927F, 0.0F));

		PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition arm_r1 = bone2.addOrReplaceChild("arm_r1", CubeListBuilder.create().texOffs(27, 6).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -5.0767F, -6.9556F, -0.6545F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		//bone2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return root;
	}

	public ModelPart getArmRight() {
		return this.armRight;
	}

	@Override
	public void translateToHand(HumanoidArm p_102108_, PoseStack p_102109_) {
		this.armRight.translateAndRotate(p_102109_);
	}

	@Override
	public void setupAnim(T p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {

	}
}