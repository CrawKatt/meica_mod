package com.crawkatt.meicamod.registry;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

/**
 * Clase que registra los puntos de interés
 * Necesario para que el Mod reconozca el portal
 * ya creado y así no generar otro al lado
**/
public class ModPOIs {
    public static final DeferredRegister<PoiType> POI =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, MeicaMod.MODID);

    public static final RegistryObject<PoiType> MEICA_PORTAL =
            POI.register("meica_portal", () -> new PoiType(Set.copyOf(ModBlocks.MEICA_PORTAL.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static void register(IEventBus eventBus) {
        POI.register(eventBus);
    }
}
