package com.crawkatt.meicamod;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.block.entity.ModBlockEntities;
import com.crawkatt.meicamod.command.SpawnClonesCommand;
import com.crawkatt.meicamod.effect.ModEffects;
import com.crawkatt.meicamod.entity.ModEntities;
import com.crawkatt.meicamod.entity.client.BrotecitoRenderer;
import com.crawkatt.meicamod.entity.client.MeicaRenderer;
import com.crawkatt.meicamod.event.*;
import com.crawkatt.meicamod.item.ModCreativeModeTabs;
import com.crawkatt.meicamod.item.ModItemProperties;
import com.crawkatt.meicamod.item.ModItems;
import com.crawkatt.meicamod.particle.ModParticles;
import com.crawkatt.meicamod.recipe.ModRecipes;
import com.crawkatt.meicamod.registry.ModPOIs;
import com.crawkatt.meicamod.screen.BrotenitaMelterScreen;
import com.crawkatt.meicamod.screen.ModMenuTypes;
import com.crawkatt.meicamod.sound.ModSounds;
import com.crawkatt.meicamod.worldgen.biome.ModTerrablender;
import com.crawkatt.meicamod.worldgen.biome.surface.ModSurfaceRules;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import terrablender.api.SurfaceRuleManager;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MeicaMod.MODID)
public class MeicaMod {
    public static final String MODID = "meicamod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MeicaMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registra las entidades
        ModEntities.register(modEventBus);

        // Registra los items
        ModItems.register(modEventBus);

        // Registra las partículas
        ModParticles.register(modEventBus);

        // Registra los audios
        ModSounds.register(modEventBus);

        // Registra los efectos
        ModEffects.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Registra los bloques
        ModBlocks.register(modEventBus);

        // Registra las pestañas del modo creativo
        ModCreativeModeTabs.register(modEventBus);

        // Registra el evento para añadir items al modo creativo
        modEventBus.addListener(this::addCreative);

        // Registra el POI (Punto de interés)
        ModPOIs.register(modEventBus);

        // Registra el Fundidor de Brotenita
        ModBlockEntities.register(modEventBus);

        // Registra los menús
        ModMenuTypes.register(modEventBus);

        // Registra las recetas
        ModRecipes.register(modEventBus);

        // Registra el setup (Necesario para que cargue las reglas de superficie)
        modEventBus.addListener(this::commonSetup);

        // Registra el evento para aplicar el efecto de la infección de brotenita
        MinecraftForge.EVENT_BUS.register(BiomeEvents.class);
        MinecraftForge.EVENT_BUS.register(DimensionEvents.class);
        MinecraftForge.EVENT_BUS.register(BossDeathHandler.class);

        // Registra el evento para añadir capabilities
        modEventBus.addListener(ModEvents::onRegisterCapabilities);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // NO COLOCAR EN EL MÉTODO CONSTRUCTOR DEL MOD
            ModTerrablender.registerBiomes();
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, ModSurfaceRules.makeRules());
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.BROTENITA_INGOT);
            event.accept(ModItems.RAW_BROTENITA);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        SpawnClonesCommand.register(event.getServer().getCommands().getDispatcher());
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                MenuScreens.register(ModMenuTypes.BROTENITA_MELTER_MENU.get(), BrotenitaMelterScreen::new);
            });
            ModItemProperties.addcustomItemProperties();

            EntityRenderers.register(ModEntities.BROTECITO.get(), BrotecitoRenderer::new);
            EntityRenderers.register(ModEntities.MEICA.get(), MeicaRenderer::new);
        }
    }
}
