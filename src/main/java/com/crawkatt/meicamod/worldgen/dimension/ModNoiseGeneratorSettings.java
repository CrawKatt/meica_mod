package com.crawkatt.meicamod.worldgen.dimension;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

import java.util.List;

public class ModNoiseGeneratorSettings {
    public static final ResourceKey<NoiseGeneratorSettings> FLOATING_FOREST = ResourceKey.create(Registries.NOISE_SETTINGS, new ResourceLocation(MeicaMod.MODID, "floating_forest"));

    private static final SurfaceRules.RuleSource GRASS_BLOCK = SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState());
    private static final SurfaceRules.RuleSource DIRT_BLOCK = SurfaceRules.state(Blocks.DIRT.defaultBlockState());
    private static final SurfaceRules.RuleSource STONE_BLOCK = SurfaceRules.state(Blocks.STONE.defaultBlockState());

    public static void bootstrap(BootstapContext<NoiseGeneratorSettings> context) {
        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
        context.register(FLOATING_FOREST, createNoiseSettings(densityFunctions));
    }

    public static NoiseGeneratorSettings createNoiseSettings(HolderGetter<DensityFunction> densityFunctions) {
        // Bloque predeterminado (grass_block) y fluido predeterminado (air)
        BlockState defaultBlock = Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState defaultFluid = Blocks.AIR.defaultBlockState();

        return new NoiseGeneratorSettings(
                // NoiseSettings (minY=0, height=128, sizeHorizontal=2, sizeVertical=1)
                new NoiseSettings(0, 128, 2, 1),
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
    private static SurfaceRules.RuleSource createSurfaceRules() {
        return SurfaceRules.sequence(
                // Si estamos en el piso, usamos pasto en la parte superior
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, GRASS_BLOCK),

                // A continuación, colocamos tierra si estamos debajo del pasto
                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, DIRT_BLOCK),

                // Si estamos más profundos, debajo de la tierra, usar piedra
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 150, CaveSurface.FLOOR), STONE_BLOCK)
        );
    }

    private static NoiseRouter createNoiseRouter(HolderGetter<DensityFunction> densityFunctions) {
        // Generar la forma de las islas flotantes del End
        DensityFunction endIslands = DensityFunctions.cache2d(DensityFunctions.endIslands(0L));

        // Obtener la función sloped_cheese del End
        DensityFunction slopedCheese = getFunction(densityFunctions, ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation("minecraft", "end/sloped_cheese")));

        // Ajustar la densidad inicial usando la función `end_islands` y sumarle un valor negativo (-0.703125)
        DensityFunction initialDensity = slideEnd(DensityFunctions.add(endIslands, DensityFunctions.constant(-0.703125)));

        // Procesar `sloped_cheese` con `postProcess`
        DensityFunction finalDensity = postProcess(slideEnd(slopedCheese));

        return new NoiseRouter(
                DensityFunctions.zero(), // barrier
                DensityFunctions.zero(), // fluid level floodedness
                DensityFunctions.zero(), // fluid level spread
                DensityFunctions.zero(), // lava
                DensityFunctions.zero(), // temperatura
                DensityFunctions.zero(), // vegetación
                DensityFunctions.zero(), // continents
                endIslands, // erosión basada en las islas del End
                DensityFunctions.zero(), // depth
                DensityFunctions.zero(), // ridges
                initialDensity, // initial_density_without_jaggedness
                finalDensity, // final_density
                DensityFunctions.zero(), // veinToggle
                DensityFunctions.zero(), // veinRidged
                DensityFunctions.zero()  // veinGap
        );
    }

    private static DensityFunction slideEnd(DensityFunction function) {
        return slide(function);
    }

    private static DensityFunction postProcess(DensityFunction densityFunction) {
        DensityFunction density = DensityFunctions.blendDensity(densityFunction);
        return DensityFunctions.mul(DensityFunctions.interpolated(density), DensityFunctions.constant(0.64)).squeeze();
    }

    // Densidad
    private static DensityFunction getFunction(HolderGetter<DensityFunction> densityFunctions, ResourceKey<DensityFunction> key) {
        return new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(key));
    }

    private static DensityFunction slide(DensityFunction densityFunction) {
        // Aplicar un gradiente para la parte superior del terreno, disminuyendo de 1 a 0 entre las alturas fromYTop y toYTop
        DensityFunction topGradient = DensityFunctions.yClampedGradient(128 - 72, 128 + 184, 1.0, 0.0);

        // Aplicar una interpolación (lerp) entre el gradiente de la parte superior y el valor de desplazamiento topOffset
        DensityFunction adjustedTop = DensityFunctions.lerp(topGradient, -23.4375, densityFunction);

        // Aplicar un gradiente para la parte inferior del terreno, aumentando de 0 a 1 entre las alturas fromYBottom y toYBottom
        DensityFunction bottomGradient = DensityFunctions.yClampedGradient(4, 32, 0.0, 1.0);

        // Devolver la función de densidad ajustada
        return DensityFunctions.lerp(bottomGradient, -0.234375, adjustedTop);
    }
}
