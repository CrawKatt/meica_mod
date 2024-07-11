package com.example.examplemod;

import com.example.examplemod.block.ModBlocks;
import com.example.examplemod.entity.ModEntities;
import com.example.examplemod.entity.client.BrotecitoRenderer;
import com.example.examplemod.entity.client.MeicaRenderer;
import com.example.examplemod.item.ModCreativeModTabs;
import com.example.examplemod.item.ModItems;
import com.example.examplemod.particle.ModParticles;
import com.example.examplemod.sound.ModSounds;
import com.example.examplemod.worldgen.biome.ModTerrablender;
import com.example.examplemod.worldgen.biome.surface.ModSurfaceRules;
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
import terrablender.api.SurfaceRuleManager;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "examplemod";

    public ExampleMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registra las entidades
        ModEntities.register(modEventBus);

        // Registra los items
        ModItems.register(modEventBus);

        // Registra las partículas
        ModParticles.register(modEventBus);

        // Registra el Bioma
        ModTerrablender.registerBiomes();

        // Registra los audios
        ModSounds.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Registra los bloques
        ModBlocks.register(modEventBus);

        // Registra las pestañas del modo creativo
        ModCreativeModTabs.register(modEventBus);

        // Registra el evento para añadir items al modo creativo
        modEventBus.addListener(this::addCreative);

        // Registra el setup (Necesario para que cargue las reglas de superficie)
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
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

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.BROTECITO.get(), BrotecitoRenderer::new);
            EntityRenderers.register(ModEntities.MEICA.get(), MeicaRenderer::new);
        }
    }
}
