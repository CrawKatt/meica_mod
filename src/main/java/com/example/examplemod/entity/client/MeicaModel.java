package com.example.examplemod.entity.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.custom.meica.MeicaEntity;
import com.example.examplemod.item.ModItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

// Dado que Meica es un Mob exactamente igual a un humano, se puede heredar de HumanoidModel
// para heredar el modelo del jugador y las animaciones de este.
public class MeicaModel<T extends MeicaEntity> extends HumanoidModel<T> {
	private static final ResourceLocation LAYER = new ResourceLocation(ExampleMod.MODID, "meica");
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(LAYER, "main");

	public MeicaModel(ModelPart modelPart) {
		super(modelPart);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partDefinition = meshDefinition.getRoot();

		return LayerDefinition.create(meshDefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		// Animación de apuntar con el arco
		ItemStack itemstack = entity.getMainHandItem();
		if (itemstack.getItem() == ModItems.MEICA_BOW.get() && entity.isUsingItem()) {
			this.rightArm.yRot = -0.1F + this.head.yRot;
			this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
			this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
		}
	}
}