package owmii.powah.client;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import owmii.powah.client.book.PowahBook;
import owmii.powah.client.handler.HudHandler;
import owmii.powah.client.handler.ReactorOverlayHandler;
import owmii.powah.client.model.PowahLayerDefinitions;
import owmii.powah.client.render.entity.EntityRenderer;
import owmii.powah.client.render.tile.TileRenderer;
import owmii.powah.client.screen.Screens;
import owmii.powah.lib.client.wiki.Wiki;

public final class PowahClient {
    public static void init() {

        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(PowahClient::clientSetup);

        modEventBus.addListener(PowahLayerDefinitions::register);
        HudHandler.register();
        modEventBus.addListener(EntityRenderer::register);
        NeoForge.EVENT_BUS.addListener(Wiki::updateRecipes);

        NeoForge.EVENT_BUS.addListener((RenderLevelStageEvent event) -> {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
                ReactorOverlayHandler.onRenderLast(event.getPoseStack());
            }
        });

    }

    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(PowahClient::clientSetupSequential);
    }

    public static void clientSetupSequential() {
        TileRenderer.register();
        Screens.register();
        ItemModelProperties.register();
        PowahBook.register();
    }
}
