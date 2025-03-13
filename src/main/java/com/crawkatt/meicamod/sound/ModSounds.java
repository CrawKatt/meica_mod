package com.crawkatt.meicamod.sound;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent MEICA_KILL_ENTITY = registerSoundEvents("meica_kill");
    public static final SoundEvent MEICA_KILL_ENTITY_LAUGHT = registerSoundEvents("meica_kill_laught");
    public static final SoundEvent MEICA_DEATH = registerSoundEvents("meica_death");
    public static final SoundEvent MEICA_DEATH_2 = registerSoundEvents("meica_death_2");
    public static final SoundEvent MEICA_DEATH_3 = registerSoundEvents("meica_death_3");
    public static final SoundEvent MEICA_LAUGHT = registerSoundEvents("meica_laught");
    public static final SoundEvent MEICA_HALLO = registerSoundEvents("meica_hallo");

    private static SoundEvent registerSoundEvents(String name) {
        Identifier identifier = new Identifier(MeicaMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void registerSounds() {
        MeicaMod.LOGGER.info("Registering Mod Sounds for " + MeicaMod.MOD_ID);
    }
}
