package com.crawkatt.meicamod.entity.client;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.meica.MeicaEntity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

// Dado que Meica es un Mob exactamente igual a un humano, se puede heredar de HumanoidModel
// para heredar el modelo del jugador y las animaciones de este.
public class MeicaModel<T extends MeicaEntity> extends HumanoidModel<T> {
	private static final ResourceLocation LAYER = new ResourceLocation(MeicaMod.MODID, "meica");
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(LAYER, "main");
	public final ModelPart leftSleeve;
	public final ModelPart rightSleeve;
	public final ModelPart leftPants;
	public final ModelPart rightPants;
	public final ModelPart jacket;

	public MeicaModel(ModelPart modelPart) {
		super(modelPart, RenderType::entityTranslucent);
		this.leftSleeve = modelPart.getChild("left_sleeve");
		this.rightSleeve = modelPart.getChild("right_sleeve");
		this.leftPants = modelPart.getChild("left_pants");
		this.rightPants = modelPart.getChild("right_pants");
		this.jacket = modelPart.getChild("jacket");
	}

	// Uso de modelo Slim por defecto
	public static LayerDefinition createBodyLayer() {
		CubeDeformation deformation = new CubeDeformation(0.0F);
		MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshDefinition.getRoot();

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

	@Override
	public void setupAnim(T entity, float animationTime, float animationSpeed, float totalTime, float yRot, float xRot) {
		super.setupAnim(entity, animationTime, animationSpeed, totalTime, yRot, xRot);

		// Necesario para que las partes del modelo sigan a las partes del cuerpo como por ejemplo:
		// las mangas sigan a los brazos, los pantalones a las piernas, la chaqueta al cuerpo, etc.
		this.leftPants.copyFrom(this.leftLeg);
		this.rightPants.copyFrom(this.rightLeg);
		this.leftSleeve.copyFrom(this.leftArm);
		this.rightSleeve.copyFrom(this.rightArm);
		this.jacket.copyFrom(this.body);
	}

	@Override
	public void setAllVisible(boolean allVisible) {
		super.setAllVisible(allVisible);
		this.leftSleeve.visible = allVisible;
		this.rightSleeve.visible = allVisible;
		this.leftPants.visible = allVisible;
		this.rightPants.visible = allVisible;
		this.jacket.visible = allVisible;
	}

	@Override
	public void setupAttackAnimation(T pLivingEntity, float pAgeInTicks) {
		// Si la entidad lleva un arco, se aplicará la animación de ataque del arco.
		ItemStack mainHandItem = pLivingEntity.getMainHandItem();
		if (pLivingEntity.isAggressive() && !mainHandItem.isEmpty()) {
			float attackSwingProgress = Mth.sin(this.attackTime * Mth.PI);
			float attackAnimationProgress = Mth.sin((1.0f - (1.0f - this.attackTime) * (1.0f - this.attackTime)) * Mth.PI);
			rightArm.zRot = 0.0f;
			leftArm.zRot = 0.0f;
			rightArm.yRot = -(0.1f - attackSwingProgress * 0.6f);
			leftArm.yRot = 0.1f - attackSwingProgress * 0.6f;
			rightArm.xRot = -1.5707964f;
			leftArm.xRot = -1.5707964f;
			var var10000 = this.rightArm;
			var10000.xRot -= attackSwingProgress * 1.2f - attackAnimationProgress * 0.4f;
			var10000 = this.leftArm;
			var10000.xRot -= attackSwingProgress * 1.2f - attackAnimationProgress * 0.4f;
			AnimationUtils.bobArms(this.rightArm, this.leftArm, pAgeInTicks);
		}

		super.setupAttackAnimation(pLivingEntity, pAgeInTicks);
		this.leftSleeve.copyFrom(this.leftArm);
		this.rightSleeve.copyFrom(this.rightArm);
	}
}