package com.crawkatt.meicamod.worldgen;

import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class ModOrePlacement {
    public static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, SquarePlacementModifier.of(), p_195348_, BiomePlacementModifier.of());
    }

    public static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
        return orePlacement(CountPlacementModifier.of(pCount), pHeightRange);
    }

    public static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
        return orePlacement(RarityFilterPlacementModifier.of(pChance), pHeightRange);
    }
}
