package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.block.custom.BrotenitaCropBlock;
import com.crawkatt.meicamod.item.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateModelGenerator.BlockTexturePool brotenitaPool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.BROTENITA_BLOCK);

        blockStateModelGenerator.registerAmethyst(ModBlocks.BROTENITA);
        blockStateModelGenerator.registerAmethyst(ModBlocks.RAW_BROTENITA);

        brotenitaPool.stairs(ModBlocks.BROTENITA_STAIRS);
        brotenitaPool.slab(ModBlocks.BROTENITA_SLAB);
        brotenitaPool.button(ModBlocks.BROTENITA_BUTTON);
        brotenitaPool.pressurePlate(ModBlocks.BROTENITA_PRESSURE_PLATE);
        brotenitaPool.fence(ModBlocks.BROTENITA_FENCE);
        brotenitaPool.fenceGate(ModBlocks.BROTENITA_FENCE_GATE);
        brotenitaPool.wall(ModBlocks.BROTENITA_WALL);

        blockStateModelGenerator.registerDoor(ModBlocks.BROTENITA_DOOR);
        blockStateModelGenerator.registerTrapdoor(ModBlocks.BROTENITA_TRAPDOOR);

        blockStateModelGenerator.registerParentedItemModel(ModItems.MEICA_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));
        blockStateModelGenerator.registerParentedItemModel(ModItems.BROTECITO_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));
        blockStateModelGenerator.registerParentedItemModel(ModItems.BROTECITO_MAMADO_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));
        blockStateModelGenerator.registerParentedItemModel(ModItems.PLAYER_CLONE_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));

        makeCrop(blockStateModelGenerator, ModBlocks.BROTENITA_CROP, BrotenitaCropBlock.AGE, 0, 1, 2, 3, 4, 5);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModBlocks.BROTENITA.asItem(), Models.GENERATED);

        itemModelGenerator.register(ModItems.BROTENITA_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_BROTENITA, Models.GENERATED);
        itemModelGenerator.register(ModItems.BROTENITA_MEAL, Models.GENERATED);

        itemModelGenerator.register(ModItems.BROTENITA_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BROTENITA_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BROTENITA_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BROTENITA_HOE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BROTENITA_SWORD, Models.HANDHELD);

        itemModelGenerator.registerArmor(((ArmorItem) ModItems.BROTENITA_HELMET));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.BROTENITA_CHESTPLATE));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.BROTENITA_LEGGINGS));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.BROTENITA_BOOTS));
    }

    public final void makeCrop(BlockStateModelGenerator generator, Block crop, Property<Integer> ageProperty, int... ageTextureIndices) {
        if (ageProperty.getValues().size() != ageTextureIndices.length) {
            throw new IllegalArgumentException();
        } else {
            Int2ObjectMap<Identifier> textureMap = new Int2ObjectOpenHashMap<>();
            BlockStateVariantMap variantMap = BlockStateVariantMap.create(ageProperty).register(age -> {
                int textureIndex = ageTextureIndices[age];
                Identifier modelId = textureMap.computeIfAbsent(textureIndex, idx ->
                        createSubModel(generator, crop, "_stage" + idx, TextureMap::cross)
                );
                return BlockStateVariant.create().put(VariantSettings.MODEL, modelId);
            });

            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(crop).coordinate(variantMap));
            registerItemModel(generator, crop.asItem());
        }
    }

    private Identifier createSubModel(BlockStateModelGenerator generator, Block block, String suffix, Function<Identifier, TextureMap> textures) {
        return Models.CROSS.upload(block, suffix, textures.apply(TextureMap.getSubId(block, suffix)), generator.modelCollector);
    }

    private void registerItemModel(BlockStateModelGenerator generator, Item item) {
        Models.GENERATED.upload(ModelIds.getItemModelId(item), TextureMap.layer0(item), generator.modelCollector);
    }
}