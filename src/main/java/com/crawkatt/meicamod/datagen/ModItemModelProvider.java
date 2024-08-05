package com.crawkatt.meicamod.datagen;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.5f);
        trimMaterials.put(TrimMaterials.IRON, 0.5f);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.5f);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.5f);
        trimMaterials.put(TrimMaterials.COPPER, 0.5f);
        trimMaterials.put(TrimMaterials.GOLD, 0.5f);
        trimMaterials.put(TrimMaterials.EMERALD, 0.5f);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.5f);
        trimMaterials.put(TrimMaterials.LAPIS, 0.5f);
        trimMaterials.put(TrimMaterials.AMETHYST, 0.5f);
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MeicaMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.BROTENITA_INGOT);
        simpleItem(ModItems.RAW_BROTENITA);
        simpleItem(ModItems.BROTENITA_MEAL);

        simpleBlockItem(ModBlocks.BROTENITA_DOOR);

        fenceItem(ModBlocks.BROTENITA_FENCE, ModBlocks.BROTENITA_BLOCK);
        buttonItem(ModBlocks.BROTENITA_BUTTON, ModBlocks.BROTENITA_BLOCK);
        wallItem(ModBlocks.BROTENITA_WALL, ModBlocks.BROTENITA_BLOCK);

        evenSimplerBlockItem(ModBlocks.BROTENITA_STAIRS);
        evenSimplerBlockItem(ModBlocks.BROTENITA_SLAB);
        evenSimplerBlockItem(ModBlocks.BROTENITA_PRESSURE_PLATE);
        evenSimplerBlockItem(ModBlocks.BROTENITA_FENCE_GATE);

        trapdoorItem(ModBlocks.BROTENITA_TRAPDOOR);

        handheldItem(ModItems.BROTENITA_SWORD);
        handheldItem(ModItems.BROTENITA_PICKAXE);
        handheldItem(ModItems.BROTENITA_AXE);
        handheldItem(ModItems.BROTENITA_SHOVEL);
        handheldItem(ModItems.BROTENITA_HOE);

        trimmedArmorItem(ModItems.BROTENITA_HELMET);
        trimmedArmorItem(ModItems.BROTENITA_CHESTPLATE);
        trimmedArmorItem(ModItems.BROTENITA_LEGGINGS);
        trimmedArmorItem(ModItems.BROTENITA_BOOTS);
    }

    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MODID = MeicaMod.MODID;

        if (itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.entrySet().forEach(entry -> {

                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + armorItem;
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(MODID, armorItemPath);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath);
                ResourceLocation trimNameResLoc = new ResourceLocation(MODID, currentTrimName);

                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                this.withExistingParent(itemRegistryObject.getId().getPath(),
                        mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                new ResourceLocation(MODID,
                                        "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MeicaMod.MODID, "item/" + item.getId().getPath()));
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(MeicaMod.MODID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_bottom"));
    }

    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  new ResourceLocation(MeicaMod.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture",  new ResourceLocation(MeicaMod.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  new ResourceLocation(MeicaMod.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(MeicaMod.MODID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MeicaMod.MODID,"item/" + item.getId().getPath()));
    }
}
