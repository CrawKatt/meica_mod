package com.example.examplemod.entity.client.renderer;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.client.model.MeicaModel;
import com.example.examplemod.entity.custom.MeicaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;


public class MeicaRenderer extends ExtendedGeoEntityRenderer<MeicaEntity> {
    public MeicaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MeicaModel());
        this.shadowRadius = 0.3f;
    }

    // Define la textura del modelo de Meica
    @Override
    public ResourceLocation getTextureLocation(MeicaEntity instance) {
        return new ResourceLocation(ExampleMod.MODID, "textures/entity/meica/meica.png");
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String s, MeicaEntity meicaEntity) {
        return null;
    }

    // Equipar un objeto en la mano derecha del modelo de Meica
    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, MeicaEntity meicaEntity) {
        if ("RightHand".equals(boneName)) {
            return new ItemStack(Items.BOW);
        }
        return ItemStack.EMPTY;
    }

    // Define la posición de la mano derecha del modelo de Meica
    @Override
    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack itemStack, String boneName) {
        if (itemStack.getItem() == Items.BOW) {
            return ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
        }
        return ItemTransforms.TransformType.NONE;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, MeicaEntity meicaEntity) {
        return null;
    }

    // Define la posición de la mano derecha del modelo de Meica
    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack itemStack, String boneName, MeicaEntity meicaEntity, IBone iBone) {
        poseStack.translate(0.0D, 0.0D, 0.0D);
    }

    @Override
    protected void preRenderBlock(BlockState blockState, String boneName, MeicaEntity meicaEntity) {

    }

    // Define la posición de la mano derecha del modelo de Meica
    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack itemStack, String boneName, MeicaEntity meicaEntity, IBone iBone) {
        poseStack.translate(0.0D, 0.0D, 0.0D);
    }

    @Override
    protected void postRenderBlock(BlockState blockState, String s, MeicaEntity meicaEntity) {

    }

    // Define el tamaño del modelo de Meica en el juego
    @Override
    public RenderType getRenderType(
            MeicaEntity animatable,
            float partialTicks,
            PoseStack stack,
            MultiBufferSource renderTypeBuffer,
            VertexConsumer vertexBuilder,
            int packedLightIn,
            ResourceLocation textureLocation
    ) {
        // Define el tamaño del modelo de Meica en el juego (1.0F, 1.0F, 1.0F es el equivalente al tamaño del modelo del jugador)
        stack.scale(1.0F, 1.0F, 1.0F);

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}