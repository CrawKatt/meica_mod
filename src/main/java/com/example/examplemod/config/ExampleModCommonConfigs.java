package com.example.examplemod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ExampleModCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("Configs for Example Mod");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
