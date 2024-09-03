package com.crawkatt.meicamod.sound;

import com.crawkatt.meicamod.MeicaMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MeicaMod.MODID);

    public static final RegistryObject<SoundEvent> MEICA_KILL_ENTITY = registerSoundEvents("meica_kill");
    public static final RegistryObject<SoundEvent> MEICA_DEATH = registerSoundEvents("meica_death");
    public static final RegistryObject<SoundEvent> MEICA_LAUGHT = registerSoundEvents("meica_laught");
    public static final RegistryObject<SoundEvent> MEICA_HALLO = registerSoundEvents("meica_hallo");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        ResourceLocation id = new ResourceLocation(MeicaMod.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
