package com.example.examplemod.entity.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.meica.MeicaEntity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// Dado que Meica es un Mob exactamente igual a un humano, se puede heredar de HumanoidModel
// para heredar el modelo del jugador y las animaciones de este.
public class MeicaModel<T extends MeicaEntity> extends HumanoidModel<T> {
	private static final ResourceLocation LAYER = new ResourceLocation(ExampleMod.MODID, "meica");
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(LAYER, "main");
	private static final String EAR = "ear";
	private static final String CLOAK = "cloak";
	private static final String LEFT_SLEEVE = "left_sleeve";
	private static final String RIGHT_SLEEVE = "right_sleeve";
	private static final String LEFT_PANTS = "left_pants";
	private static final String RIGHT_PANTS = "right_pants";
	private final List<ModelPart> parts;
	public final ModelPart leftSleeve;
	public final ModelPart rightSleeve;
	public final ModelPart leftPants;
	public final ModelPart rightPants;
	public final ModelPart jacket;
	private final ModelPart cloak;
	private final ModelPart ear;

	public MeicaModel(ModelPart modelPart) {
		super(modelPart, RenderType::entityTranslucent);
		this.ear = modelPart.getChild("ear");
		this.cloak = modelPart.getChild("cloak");
		this.leftSleeve = modelPart.getChild("left_sleeve");
		this.rightSleeve = modelPart.getChild("right_sleeve");
		this.leftPants = modelPart.getChild("left_pants");
		this.rightPants = modelPart.getChild("right_pants");
		this.jacket = modelPart.getChild("jacket");
		this.parts = modelPart.getAllParts().filter((part) -> !part.isEmpty()).collect(ImmutableList.toImmutableList());
	}

	// Uso de modelo Slim por defecto
	public static LayerDefinition createBodyLayer() {
		CubeDeformation deformation = new CubeDeformation(0.0F);
		MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshDefinition.getRoot();
		partdefinition.addOrReplaceChild("ear", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, deformation), PartPose.ZERO);
		partdefinition.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, deformation, 1.0F, 0.5F), PartPose.offset(0.0F, 0.0F, 0.0F));
		float f = 0.25F;
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation), PartPose.offset(5.0F, 2.5F, 0.0F));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation), PartPose.offset(-5.0F, 2.5F, 0.0F));
        partdefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.offset(5.0F, 2.5F, 0.0F));
        partdefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.offset(-5.0F, 2.5F, 0.0F));

        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(1.9F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);

		return LayerDefinition.create(meshDefinition, 64, 64);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return Iterables.concat(super.bodyParts(), ImmutableList.of(this.leftPants, this.rightPants, this.leftSleeve, this.rightSleeve, this.jacket));
	}

	public void renderEars(PoseStack p_103402_, VertexConsumer consumer, int p_103404_, int p_103405_) {
		this.ear.copyFrom(this.head);
		this.ear.x = 0.0F;
		this.ear.y = 0.0F;
		this.ear.render(p_103402_, consumer, p_103404_, p_103405_);
	}

	public void renderCloak(PoseStack stack, VertexConsumer consumer, int p_103414_, int p_103415_) {
		this.cloak.render(stack, consumer, p_103414_, p_103415_);
	}

	@Override
	public void setupAnim(T p_103395_, float p_103396_, float p_103397_, float p_103398_, float p_103399_, float p_103400_) {
		super.setupAnim(p_103395_, p_103396_, p_103397_, p_103398_, p_103399_, p_103400_);

		// Necesario para que las partes del modelo sigan a las partes del cuerpo como por ejemplo:
		// las mangas sigan a los brazos, los pantalones a las piernas, la chaqueta al cuerpo, etc.
		this.leftPants.copyFrom(this.leftLeg);
		this.rightPants.copyFrom(this.rightLeg);
		this.leftSleeve.copyFrom(this.leftArm);
		this.rightSleeve.copyFrom(this.rightArm);
		this.jacket.copyFrom(this.body);
		if (p_103395_.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
			if (p_103395_.isCrouching()) {
				this.cloak.z = 1.4F;
				this.cloak.y = 1.85F;
			} else {
				this.cloak.z = 0.0F;
				this.cloak.y = 0.0F;
			}
		} else if (p_103395_.isCrouching()) {
			this.cloak.z = 0.3F;
			this.cloak.y = 0.8F;
		} else {
			this.cloak.z = -1.1F;
			this.cloak.y = -0.85F;
		}
	}

	@Override
	public void setAllVisible(boolean allVisible) {
		super.setAllVisible(allVisible);
		this.leftSleeve.visible = allVisible;
		this.rightSleeve.visible = allVisible;
		this.leftPants.visible = allVisible;
		this.rightPants.visible = allVisible;
		this.jacket.visible = allVisible;
		this.cloak.visible = allVisible;
		this.ear.visible = allVisible;
	}

	@Override
	public void setupAttackAnimation(T pLivingEntity, float pAgeInTicks) {
		// Si la entidad lleva un arco, se aplicará la animación de ataque del arco.
		ItemStack stack = pLivingEntity.getMainHandItem();
		if (pLivingEntity.isAggressive() && !stack.isEmpty()) {
			float attack = Mth.sin(this.attackTime * 3.1415927f);
			float animation = Mth.sin((1.0f - (1.0f - this.attackTime) * (1.0f - this.attackTime)) * 3.1415927f);
			rightArm.zRot = 0.0f;
			leftArm.zRot = 0.0f;
			rightArm.yRot = -(0.1f - attack * 0.6f);
			leftArm.yRot = 0.1f - attack * 0.6f;
			rightArm.xRot = -1.5707964f;
			leftArm.xRot = -1.5707964f;
			var var10000 = this.rightArm;
			var10000.xRot -= attack * 1.2f - animation * 0.4f;
			var10000 = this.leftArm;
			var10000.xRot -= attack * 1.2f - animation * 0.4f;
			AnimationUtils.bobArms(this.rightArm, this.leftArm, pAgeInTicks);
		}

		super.setupAttackAnimation(pLivingEntity, pAgeInTicks);
		this.leftSleeve.copyFrom(this.leftArm);
		this.rightSleeve.copyFrom(this.rightArm);
	}
}