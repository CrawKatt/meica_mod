package com.crawkatt.meicamod.screen;

import com.crawkatt.meicamod.MeicaMod;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<BrotecitoScreenHandler> BROTECITO_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(MeicaMod.MOD_ID, "brotecito_screen_handler"),
                    new ExtendedScreenHandlerType<>(BrotecitoScreenHandler::new));

    public static void registerScreenHandlers() {
        MeicaMod.LOGGER.info("Registering Screen Handlers for " + MeicaMod.MOD_ID);
    }
}