package com.crawkatt.meicamod.worldgen.dimension;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseRouter;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

import java.util.List;

public class ModNoiseGeneratorSettings {
    public static final RegistryKey<ChunkGeneratorSettings> FLOATING_FOREST = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, new Identifier(MeicaMod.MOD_ID, "floating_forest"));

    private static final MaterialRules.MaterialRule GRASS_BLOCK = MaterialRules.block(Blocks.GRASS_BLOCK.getDefaultState());
    private static final MaterialRules.MaterialRule DIRT_BLOCK = MaterialRules.block(Blocks.DIRT.getDefaultState());
    private static final MaterialRules.MaterialRule STONE_BLOCK = MaterialRules.block(Blocks.STONE.getDefaultState());

    public static void bootstrap(Registerable<ChunkGeneratorSettings> context) {
        RegistryEntryLookup<DensityFunction> DensityFunctionsTypes = context.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION);
        context.register(FLOATING_FOREST, createNoiseSettings(DensityFunctionsTypes));
    }

    public static ChunkGeneratorSettings createNoiseSettings(RegistryEntryLookup<DensityFunction> densityFunctions) {
        // Bloque predeterminado (grass_block) y fluido predeterminado (air)
        BlockState defaultBlock = Blocks.GRASS_BLOCK.getDefaultState();
        BlockState defaultFluid = Blocks.AIR.getDefaultState();

        return new ChunkGeneratorSettings(
                // NoiseSettings (minY=0, height=128, sizeHorizontal=2, sizeVertical=1)
                new GenerationShapeConfig(0, 128, 2, 1),
                defaultBlock, // defaultBlock
                defaultFluid, // defaultFluid
                createNoiseRouter(densityFunctions), // noiseRouter
                createSurfaceRules(), // surfaceRule (regla de superficie con grass_block)
                List.of(), // spawnTarget
                0, // seaLevel (nivel del mar = 0)
                true, // disableMobGeneration
                false, // aquifersEnabled
                false, // oreVeinsEnabled
                true  // useLegacyRandomSource
        );
    }

    // Crear las reglas de superficie para el bloque de grass
    private static MaterialRules.MaterialRule createSurfaceRules() {
        return MaterialRules.sequence(
                // Si estamos en el piso, usamos pasto en la parte superior
                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, GRASS_BLOCK),

                // A continuación, colocamos tierra si estamos debajo del pasto
                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH, DIRT_BLOCK),

                // Si estamos más profundos, debajo de la tierra, usar piedra
                MaterialRules.condition(MaterialRules.stoneDepth(0, false, 150, VerticalSurfaceType.FLOOR), STONE_BLOCK)
        );
    }

    private static NoiseRouter createNoiseRouter(RegistryEntryLookup<DensityFunction> densityFunctions) {
        // Generar la forma de las islas flotantes del End
        DensityFunction endIslands = DensityFunctionTypes.cache2d(DensityFunctionTypes.endIslands(0L));

        // Obtener la función sloped_cheese del End
        DensityFunction slopedCheese = getFunction(densityFunctions, RegistryKey.of(RegistryKeys.DENSITY_FUNCTION, new Identifier("minecraft", "end/sloped_cheese")));

        // Ajustar la densidad inicial usando la función `end_islands` y sumarle un valor negativo (-0.703125)
        DensityFunction initialDensity = slideEnd(DensityFunctionTypes.add(endIslands, DensityFunctionTypes.constant(-0.703125)));

        // Procesar `sloped_cheese` con `postProcess`
        DensityFunction finalDensity = postProcess(slideEnd(slopedCheese));

        return new NoiseRouter(
                DensityFunctionTypes.zero(), // barrier
                DensityFunctionTypes.zero(), // fluid level floodedness
                DensityFunctionTypes.zero(), // fluid level spread
                DensityFunctionTypes.zero(), // lava
                DensityFunctionTypes.zero(), // temperatura
                DensityFunctionTypes.zero(), // vegetación
                DensityFunctionTypes.zero(), // continents
                endIslands, // erosión basada en las islas del End
                DensityFunctionTypes.zero(), // depth
                DensityFunctionTypes.zero(), // ridges
                initialDensity, // initial_density_without_jaggedness
                finalDensity, // final_density
                DensityFunctionTypes.zero(), // veinToggle
                DensityFunctionTypes.zero(), // veinRidged
                DensityFunctionTypes.zero()  // veinGap
        );
    }

    private static DensityFunction slideEnd(DensityFunction function) {
        return slide(function);
    }

    private static DensityFunction postProcess(DensityFunction densityFunction) {
        DensityFunction density = DensityFunctionTypes.blendDensity(densityFunction);
        return DensityFunctionTypes.mul(DensityFunctionTypes.interpolated(density), DensityFunctionTypes.constant(0.64)).squeeze();
    }

    // Densidad
    private static DensityFunction getFunction(RegistryEntryLookup<DensityFunction> DensityFunctionsTypes, RegistryKey<DensityFunction> key) {
        return new DensityFunctionTypes.RegistryEntryHolder(DensityFunctionsTypes.getOrThrow(key));
    }

    private static DensityFunction slide(DensityFunction densityFunction) {
        // Aplicar un gradiente para la parte superior del terreno, disminuyendo de 1 a 0 entre las alturas fromYTop y toYTop
        DensityFunction topGradient = DensityFunctionTypes.yClampedGradient(128 - 72, 128 + 184, 1.0, 0.0);

        // Aplicar una interpolación (lerp) entre el gradiente de la parte superior y el valor de desplazamiento topOffset
        DensityFunction adjustedTop = DensityFunctionTypes.lerp(topGradient, -23.4375, densityFunction);

        // Aplicar un gradiente para la parte inferior del terreno, aumentando de 0 a 1 entre las alturas fromYBottom y toYBottom
        DensityFunction bottomGradient = DensityFunctionTypes.yClampedGradient(4, 32, 0.0, 1.0);

        // Devolver la función de densidad ajustada
        return DensityFunctionTypes.lerp(bottomGradient, -0.234375, adjustedTop);
    }
}
