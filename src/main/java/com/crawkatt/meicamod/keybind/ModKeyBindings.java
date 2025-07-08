package com.crawkatt.meicamod.keybind;

import com.crawkatt.meicamod.MeicaMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ModKeyBindings {
    public static KeyBinding OPEN_SPELL_WHEEL;

    public static void register() {
        OPEN_SPELL_WHEEL = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + MeicaMod.MOD_ID + ".open_spell_wheel",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category." + MeicaMod.MOD_ID + ".keys"
        ));
    }
}
