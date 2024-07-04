package com.example.examplemod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;

public class BrotecitoModel<T extends Entity> extends HierarchicalModel<T> implements ArmedModel, HeadedModel {
	private final ModelPart brotecito;
	private final ModelPart arms;
	private final ModelPart pot;
	private final ModelPart rightArm;
	private final ModelPart leaf;
	private final ModelPart head;
	private final ModelPart leftArm;
	private final ModelPart frontPot;
	private final ModelPart backPot;
	private final ModelPart leftPot;
	private final ModelPart rightPot;
	private final ModelPart upperLeftPot;
	private final ModelPart upperRightPot;
	private final ModelPart upperFrontPot;
	private final ModelPart upperBackPot;
	private final ModelPart bottomPotFaceAndSoil;

	public BrotecitoModel(ModelPart root) {
		this.brotecito = root.getChild("brotecito");
		this.leaf = brotecito.getChild("leaf");
		this.head = brotecito.getChild("head");
		this.arms = brotecito.getChild("arms");
		this.pot = brotecito.getChild("pot");

		this.leftArm = arms.getChild("leftArm");
		this.rightArm = arms.getChild("rightArm");

		this.frontPot = pot.getChild("frontPot");
		this.backPot = pot.getChild("backPot");
		this.leftPot = pot.getChild("leftPot");
		this.rightPot = pot.getChild("rightPot");
		this.upperLeftPot = pot.getChild("upperLeftPot");
		this.upperRightPot = pot.getChild("upperRightPot");
		this.upperFrontPot = pot.getChild("upperFrontPot");
		this.upperBackPot = pot.getChild("upperBackPot");
		this.bottomPotFaceAndSoil = pot.getChild("bottomPotFaceAndSoil");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition brotecito = partdefinition.addOrReplaceChild("brotecito", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition leaf = brotecito.addOrReplaceChild("leaf", CubeListBuilder.create().texOffs(1, 4).addBox(1.3045F, 1.7638F, 2.1262F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3045F, -13.7638F, 4.1262F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition leafLower_r1 = leaf.addOrReplaceChild("leafLower_r1", CubeListBuilder.create().texOffs(53, 44).addBox(-2.3827F, -0.2609F, 1.5675F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(53, 41).addBox(-2.3827F, -7.2609F, 1.5675F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(51, 18).addBox(-3.3827F, -6.2609F, 1.5675F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3394F, 2.5155F, -1.0048F, 0.7854F, 0.3927F, 0.0F));
		PartDefinition leaftRoot2_r1 = leaf.addOrReplaceChild("leaftRoot2_r1", CubeListBuilder.create().texOffs(1, 1).addBox(-0.8827F, -1.9942F, 1.1189F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5926F, 3.4932F, 0.7139F, 0.3927F, 0.3927F, 0.0F));
		PartDefinition head = brotecito.addOrReplaceChild("head", CubeListBuilder.create().texOffs(41, 1).addBox(-4.9286F, -2.3571F, 4.5F, 10.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(26, 28).addBox(-4.9286F, -3.3571F, -4.5F, 10.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(27, 19).addBox(-3.9286F, -4.3571F, -3.5F, 8.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(1, 8).addBox(-3.9286F, 3.6429F, -4.5F, 8.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(1, 35).addBox(-5.9286F, -2.3571F, -4.5F, 2.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(10, 19).addBox(3.0714F, -2.3571F, -4.5F, 3.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0714F, -6.6429F, -0.5F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition backFace_r1 = head.addOrReplaceChild("backFace_r1", CubeListBuilder.create().texOffs(28, 0).addBox(4.0F, -8.0F, -5.0F, 1.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0714F, 5.6429F, -0.5F, 0.0F, 1.5708F, 0.0F));
		PartDefinition arms = brotecito.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightArm = arms.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, -5.0767F, -2.9556F, -0.2182F, 0.0F, 0.0F));
		PartDefinition rightArm_r1 = rightArm.addOrReplaceChild("rightArm_r1", CubeListBuilder.create().texOffs(21, 1).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));
		PartDefinition leftArm = arms.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, -5.0767F, -2.9556F, -0.2182F, 0.0F, 0.0F));
		PartDefinition leftArm_r1 = leftArm.addOrReplaceChild("leftArm_r1", CubeListBuilder.create().texOffs(7, 1).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));
		PartDefinition pot = brotecito.addOrReplaceChild("pot", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition frontPot = pot.addOrReplaceChild("frontPot", CubeListBuilder.create().texOffs(33, 62).addBox(-7.0F, -2.0F, 6.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition backPot = pot.addOrReplaceChild("backPot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition backPot_r1 = backPot.addOrReplaceChild("backPot_r1", CubeListBuilder.create().texOffs(33, 59).addBox(-7.0F, -2.0F, 6.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
		PartDefinition leftPot = pot.addOrReplaceChild("leftPot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftPot_r1 = leftPot.addOrReplaceChild("leftPot_r1", CubeListBuilder.create().texOffs(1, 53).addBox(-6.0F, -2.0F, 6.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition rightPot = pot.addOrReplaceChild("rightPot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightPot_r1 = rightPot.addOrReplaceChild("rightPot_r1", CubeListBuilder.create().texOffs(1, 56).addBox(-6.0F, -2.0F, 6.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition upperLeftPot = pot.addOrReplaceChild("upperLeftPot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition upperLeftPot_r1 = upperLeftPot.addOrReplaceChild("upperLeftPot_r1", CubeListBuilder.create().texOffs(0, 59).addBox(-8.0F, -3.0F, 7.0F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition upperRightPot = pot.addOrReplaceChild("upperRightPot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition upperRightPot_r1 = upperRightPot.addOrReplaceChild("upperRightPot_r1", CubeListBuilder.create().texOffs(29, 53).addBox(-8.0F, -3.0F, -8.0F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition upperFrontPot = pot.addOrReplaceChild("upperFrontPot", CubeListBuilder.create().texOffs(0, 62).addBox(-8.0F, -3.0F, 7.0F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition upperBackPot = pot.addOrReplaceChild("upperBackPot", CubeListBuilder.create().texOffs(33, 56).addBox(-7.0F, -3.0F, -8.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition bottomPotFaceAndSoil = pot.addOrReplaceChild("bottomPotFaceAndSoil", CubeListBuilder.create().texOffs(15, 39).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		brotecito.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return brotecito;
	}

	public void translateToRightArm(PoseStack poseStack) {
		// Ajustar los valores según sea necesario para la ubicación/posición correcta de la espada
		// X: Ok (-0.4F)
		// Y: Ok (0.6F)
		// Z: Ok (0.0F) [Aumentar el valor de Z en Negativo hace que la espada se vaya hacia adelante, hacer lo mismo en Positivo hace que vaya hacia atrás]
		poseStack.translate(-0.4F, 0.6F, 0.0F);

		// Ok(-20.5F) [Aumentar el valor de X en Negativo hace que la espada se incline hacia arriba, hacer lo mismo en Positivo hace que se incline hacia abajo]
		poseStack.mulPose(Axis.XP.rotationDegrees(-20.5F)); // Usar rotaciones del modelo
		poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(0.0F));
	}

	@Override
	public void translateToHand(HumanoidArm p_102108_, PoseStack p_102109_) {
		System.out.println("RightArm: " + p_102109_);
		this.translateToRightArm(p_102109_);
	}

	@Override
	public ModelPart getHead() {
		return this.head;
	}

	@Override
	public void setupAnim(T p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {

	}
}