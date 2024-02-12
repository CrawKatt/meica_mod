package com.example.examplemod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ExampleModClientConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("Configs for Example Mod");

        // Definir configuraciones aquí
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
