package com.crawkatt.meicamod.worldgen.dimension;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.OptionalLong;

public class ModDimensions {
    public static final RegistryKey<DimensionOptions> MEICADIM_KEY = RegistryKey.of(RegistryKeys.DIMENSION,
            new Identifier(MeicaMod.MOD_ID, "meicadim"));
    public static final RegistryKey<World> MEICADIM_WORLD_KEY = RegistryKey.of(RegistryKeys.WORLD,
            new Identifier(MeicaMod.MOD_ID, "meicadim"));
    public static final RegistryKey<DimensionType> MEICA_DIM_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            new Identifier(MeicaMod.MOD_ID, "meicadim_type"));

    public static void boostrapType(Registerable<DimensionType> context) {
        context.register(MEICA_DIM_TYPE, new DimensionType(
                OptionalLong.of(1200), // define la hora de la dimension (fija)
                false, // hasSkyLight
                false, // hasCeiling
                false, // ultrawarm
                false, // natural
                1.0D, // coordinateScale
                false, // bedWorks
                false, // respawnAnchorWorks
                0, // minY
                256, // height
                256, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                DimensionTypes.THE_NETHER_ID, // effectsLocation
                0.5f, // ambientLight
                new DimensionType.MonsterSettings(true, false, ConstantIntProvider.create(0), 0)));
    }
}
