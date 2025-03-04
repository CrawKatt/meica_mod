package com.crawkatt.meicamod.worldgen.gen;

public class ModWorldGeneration {
    public static void generateModWorldGeneration() {
        ModGeodeGeneration.generateGeodes();
        ModOreGeneration.generateOres();
        ModTreeGeneration.generateTrees();
    }
}
